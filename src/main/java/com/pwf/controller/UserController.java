package com.pwf.controller;

import com.pwf.domain.PageBean;
import com.pwf.domain.User;
import com.pwf.service.UserService;
import com.pwf.util.ConstraintViolationExceptionHandler;
import com.pwf.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
@Api(tags = "后台用户控制层")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/me")
    @ApiOperation(value = "查看当前用户基本信息")
    public Object getCurrent(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
//        public Object getCurrent(){//Authentication authentication){ 获得详细用户信息
//        return SecurityContextHolder.getContext().getAuthentication();
//    }

    /**
     * 查询用户
     *
     * @return
     */
    @GetMapping("/usersSearch")
    public ModelAndView list(/*@RequestParam(value="async",required=false) boolean async,
                             @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                             @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,*/
                             @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                             Model model) {

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = userService.listUsersByNameLike(searchText, pageable);
        List<User> list = page.getContent();    // 当前所在页面数据列表
        log.info(list.toString());
//        model.addAttribute("page", page);
        model.addAttribute("userList", list);
        return new ModelAndView("background/user-tables", "userModel", model);
    }

    //    @PreAuthorize("hasRole('admin')")
    @GetMapping("/users")
    @ApiOperation(value = "根据创建时间分页查询所有用户数据")
    private ModelAndView queryAll(PageBean pageBean, Model model) {
        Page<User> userPage = userService.listUsersPage(pageBean);
        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("totalPage", userPage.getTotalPages());
        model.addAttribute("currentPage", pageBean.getPage());
//        return "admin/person-list";
//        model.addAttribute("userList", userService.listUsers());
        return new ModelAndView("background/user-tables", "userModel", model);
    }

    @PostMapping({"/users", "/update"})
    @ApiOperation(value = "注册、修改、保存用户")
    public ResponseEntity<ResultVO> saveUser(User user) {
//    public ResponseEntity<ResultVO> saveUser(@Valid User user, BindingResult bindingResult){
//        String Result="";
//        if(bindingResult.hasErrors()){
//            List<ObjectError> ls=bindingResult.getAllErrors();
//            for (int i = 0; i < ls.size(); i++) {
//                System.out.println("error:"+ls.get(i));
////                Result = JSON.toJSONString(new ResultVO(false,bindingResult.getFieldError().getDefaultMessage()));
//            }
//        }
        if (user.getUserId() == null) {
            try {
                userService.save(user);
                return ResponseEntity.ok().body(new ResultVO(true, "注册成功"));
            } catch (ConstraintViolationException e) {
                return ResponseEntity.ok().body(new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e)));
            } catch (Exception e) {
                return ResponseEntity.ok().body(new ResultVO(false, e.getMessage()));
            }
        }

        try {
            userService.update(user);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResultVO(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ResultVO(true, "修改成功"));

    }

//    @PostMapping({"/users","/update"})
//    @ApiOperation(value = "注册、修改、保存用户")
//    public ResponseEntity<ResultVO> create(User user, Long authorityId) {
//        List<Authority> authorities = new ArrayList<>();
//        authorities.add(authorityService.getAuthorityById(authorityId));
//        user.setAuthorities(authorities);
//
//        if(user.getUserId() == null) {
//            user.setEncodePassword(user.getPassword()); // 加密密码
//        }else {
//            // 判断密码是否做了变更
//            User originalUser = userService.getUserById(user.getUserId());
//            String rawPassword = originalUser.getPassword();
//            PasswordEncoder encoder = new BCryptPasswordEncoder();
//            String encodePasswd = encoder.encode(user.getPassword());
//            boolean isMatch = encoder.matches(rawPassword, encodePasswd);
//            if (!isMatch) {
//                user.setEncodePassword(user.getPassword());
//            }else {
//                user.setPassWord(user.getPassword());
//            }
//        }
//        try {
//            userService.save(user);
//        }  catch (ConstraintViolationException e)  {
//            return ResponseEntity.ok().body(new ResultVO(false, ConstraintViolationExceptionHandler.getMessage(e)));
//        }
//
//        return ResponseEntity.ok().body(new ResultVO(true, "处理成功", user));
//    }

    @ApiOperation("根据用户名和id查重")
    @GetMapping("/exist")
    public boolean isExist(String userName, Integer userId) {
        return userService.isExist(userName, userId);
    }

    @ApiOperation("根据用户名查重")
    @GetMapping("/existByUsername")
    public boolean isExistByUsername(String userName) {
        return userService.isExist(userName, 0);
    }

    @ApiOperation("根据用户名和邮箱验证用户")
    @GetMapping("/existByUsernameAndEmail")
    public ResultVO existByUsernameAndEmail(String userName, String email) {
        return userService.existByUsernameAndEmail(userName, email);
    }

    @ApiOperation("根据用户id查询并转发到管理员编辑页面")
    @GetMapping("/userDetail")
    public ModelAndView editForm(@RequestParam("userId") Integer userId, Model model) {
//        User user = (User) redisTemplate.opsForValue().get("user-" + userId);
//        if (user == null) {
        User user = userService.getUserById(userId);
//            redisTemplate.opsForValue().set("user-" + userId, user);
//        }
        model.addAttribute("user", user);
        return new ModelAndView("background/user-edit", "userModel", model);
    }

//    @GetMapping("/userDelete")
//    @ApiOperation(value = "删除用户")
//    private ModelAndView deleteUser(@RequestParam("userId") Integer userId, Model model) {
//        if (userId!= 0) {
//            try {
//                userService.delete(userId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
////        model.addAttribute(dao.findAll());
//        return new ModelAndView("redirect:/user/users");
//    }

    /**
     * 返回数据 不再返回视图
     */
    @GetMapping("/userDelete")
    public Map<String, Object> delete(@RequestParam("userId") Integer userId) {
        userService.delete(userId);
        // 删除成功以后，返回数据
        Map<String, Object> r = new HashMap<>();
        r.put("code", 0);//删除成功
        r.put("msg", "删除用户成功");
        return r;
    }

    //    @DeleteMapping("/delete/all")
    @GetMapping("/userDelete/all")
    @ApiOperation("批量删除")
    public void deleteAll(@RequestParam(value = "id") Integer[] idArr) {
        userService.deleteIn(idArr);
    }

//    @ApiOperation("转发到管理员编辑页面")
//    @GetMapping("/userDetail")
//    public String editForm(@RequestParam("userId") Integer userId, Model model) {
//        if (userId!= 0) {
//            try {
//
//                Optional<User> user = userService.find(userId);
//                model.addAttribute(user);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
////        model.addAttribute(dao.findAll());
//        return "background/user-edit";
//    }
}
