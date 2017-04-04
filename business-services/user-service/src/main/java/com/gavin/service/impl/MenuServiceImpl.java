package com.gavin.service.impl;

import com.gavin.entity.MenuAuthorityEntity;
import com.gavin.entity.MenuEntity;
import com.gavin.entity.UserAuthorityEntity;
import com.gavin.entity.UserEntity;
import com.gavin.enums.AuthorityEnums;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.model.dto.user.AuthorityDto;
import com.gavin.model.dto.user.CreateMenuDto;
import com.gavin.model.vo.user.MenuVo;
import com.gavin.repository.MenuAuthorityRepository;
import com.gavin.repository.MenuRepository;
import com.gavin.repository.UserRepository;
import com.gavin.service.MenuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final ModelMapper modelMapper;

    private final MenuRepository menuRepository;

    private final UserRepository userRepository;

    private final MenuAuthorityRepository menuAuthorityRepository;

    @Autowired
    public MenuServiceImpl(
            ModelMapper modelMapper,
            UserRepository userRepository,
            MenuRepository menuRepository,
            MenuAuthorityRepository menuAuthorityRepository) {
        this.modelMapper = modelMapper;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.menuAuthorityRepository = menuAuthorityRepository;
    }

    @Override
    public String createMenu(CreateMenuDto _menu) {
        MenuEntity menuEntity = modelMapper.map(_menu, MenuEntity.class);
        menuRepository.save(menuEntity);
        return menuEntity.getId();
    }

    @Override
    @Transactional
    public void updateAuthorities(String _menuId, List<AuthorityDto> _authorities) {
        Optional.ofNullable(menuRepository.findOne(_menuId))
                .orElseThrow(() -> new RecordNotFoundException("menu", _menuId));

        // 删除原来所有权限。
        List<MenuAuthorityEntity> menuAuthorityEntities = menuAuthorityRepository.findByMenuId(_menuId);
        menuAuthorityRepository.deleteInBatch(menuAuthorityEntities);

        _authorities.forEach(
                authorityDto -> {
                    MenuAuthorityEntity menuAuthorityEntity = new MenuAuthorityEntity();
                    menuAuthorityEntity.setMenuId(_menuId);
                    menuAuthorityEntity.setAuthority(AuthorityEnums.valueOf(authorityDto.getAuthority()));
                    menuAuthorityRepository.save(menuAuthorityEntity);
                }
        );
    }

    @Override
    public List<MenuVo> acquireTreeByUserId(String _userId) {
        UserEntity userEntity = Optional.ofNullable(userRepository.findOne(_userId))
                .orElseThrow(() -> new RecordNotFoundException("user", _userId));

        List<UserAuthorityEntity> userAuthorityEntities = userEntity.getUserAuthorityEntities();
        // 获得该用户拥有的所有权限。
        List<AuthorityEnums> authorities = userAuthorityEntities.stream()
                .map(UserAuthorityEntity::getAuthority)
                .distinct()
                .collect(Collectors.toList());

        List<MenuAuthorityEntity> menuAuthorityEntities = menuAuthorityRepository.findByAuthorityIn(authorities);
        List<String> menuIds = menuAuthorityEntities.stream()
                .map(MenuAuthorityEntity::getMenuId)
                .collect(Collectors.toList());

        List<MenuEntity> menuEntities = menuRepository.findByIdIn(menuIds);

        List<MenuVo> menuVos = menuEntities.stream()
                .map(menuEntity -> modelMapper.map(menuEntity, MenuVo.class))
                .collect(Collectors.toList());

        // 获得顶层菜单。
        List<MenuVo> topMenuVos = menuVos.stream()
                .filter(menuVo -> StringUtils.isEmpty(menuVo.getParentId()))
                .sorted(Comparator.comparing(MenuVo::getValue))
                .collect(Collectors.toList());

        // 获得非顶层菜单。
        List<MenuVo> nonTopMenuVos = menuVos.stream()
                .filter(menuVo -> !StringUtils.isEmpty(menuVo.getParentId()))
                .collect(Collectors.toList());

        // 生成非顶层菜单的层次结构。
        generateHiberarchy(nonTopMenuVos, nonTopMenuVos);

        // 组装到顶层菜单下。
        generateHiberarchy(topMenuVos, nonTopMenuVos);

        return topMenuVos;
    }

    private void generateHiberarchy(List<MenuVo> parentMenuVos, List<MenuVo> subMenuVos) {
        parentMenuVos.forEach(
                parentMenuVo -> {
                    List<MenuVo> menuVos = new ArrayList<>();
                    subMenuVos.forEach(
                            subMenuVo -> {
                                if (subMenuVo.getParentId().equals(parentMenuVo.getId())) {
                                    menuVos.add(subMenuVo);
                                }
                            }
                    );
                    List<MenuVo> sortedMenuVos = menuVos.stream()
                            .sorted(Comparator.comparing(MenuVo::getValue))
                            .collect(Collectors.toList());
                    parentMenuVo.setSubMenus(sortedMenuVos);
                }
        );
    }

}
