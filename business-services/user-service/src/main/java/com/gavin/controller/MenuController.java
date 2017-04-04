package com.gavin.controller;

import com.gavin.constant.ResponseCodeConstants;
import com.gavin.model.Response;
import com.gavin.model.dto.user.AuthorityDto;
import com.gavin.model.dto.user.CreateMenuDto;
import com.gavin.model.vo.user.MenuVo;
import com.gavin.service.MenuService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/users/menus", description = "用户菜单相关API")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/users/menus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "登记单个菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<String> createMenu(
            @ApiParam(name = "menu", value = "要创建的菜单信息", required = true) @Valid @RequestBody CreateMenuDto _menu) {
        Response<String> response = new Response(ResponseCodeConstants.SUCCESS);

        String menuId = menuService.createMenu(_menu);
        log.info("菜单{}创建成功。", menuId);

        response.setContents(menuId);
        return response;
    }

    @RequestMapping(value = "/users/menus/{menu_id}/grant", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "设定菜单权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response setAuthorities(
            @ApiParam(name = "menu_id", value = "对象菜单ID", required = true) @PathVariable("menu_id") String _menuId,
            @ApiParam(name = "authorities", value = "要赋予的权限", required = true) @Valid @RequestBody List<AuthorityDto> _authorities) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);
        menuService.updateAuthorities(_menuId, _authorities);
        return response;
    }

    @RequestMapping(value = "/users/{user_id}/menus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获得指定用户的树形菜单层次")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<List<MenuVo>> acquireMenuTreeByUserId(
            @ApiParam(name = "user_id", value = "用户ID", required = true) @PathVariable("user_id") String _userId) {
        Response<List<MenuVo>> response = new Response(ResponseCodeConstants.SUCCESS);

        List<MenuVo> menuVos = menuService.acquireTreeByUserId(_userId);
        response.setContents(menuVos);
        return response;
    }

}
