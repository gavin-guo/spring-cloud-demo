package com.gavin.service;

import com.gavin.model.dto.user.AuthorityDto;
import com.gavin.model.dto.user.CreateMenuDto;
import com.gavin.model.vo.user.MenuVo;

import java.util.List;

public interface MenuService {

    /**
     * 创建菜单
     *
     * @param _menu 菜单信息
     * @return
     */
    String createMenu(CreateMenuDto _menu);

    /**
     * 更新菜单权限
     *
     * @param _menuId
     * @param _authorities
     */
    void updateAuthorities(String _menuId, List<AuthorityDto> _authorities);

    /**
     * 获得指定用户的树形结构菜单
     *
     * @param _userId 用户ID
     * @return
     */
    List<MenuVo> acquireTreeByUserId(String _userId);

}
