package com.itmo.evaluation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itmo.evaluation.common.ErrorCode;
import com.itmo.evaluation.exception.BusinessException;
import com.itmo.evaluation.mapper.BlackHouseMapper;
import com.itmo.evaluation.model.dto.StudentLoginRequest;
import com.itmo.evaluation.model.entity.BlackHouse;
import com.itmo.evaluation.model.entity.Student;
import com.itmo.evaluation.service.StudentService;
import com.itmo.evaluation.mapper.StudentMapper;
import com.itmo.evaluation.utils.JwtUtil;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static net.dreamlu.mica.core.utils.StringPool.UNKNOWN;

/**
* @author chenjiahan
* @description 针对表【e_student(学生表)】的数据库操作Service实现
* @createDate 2023-01-27 12:23:34
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

    @Resource
    private BlackHouseMapper blackHouseMapper;

    @Resource
    private Ip2regionSearcher ip2regionSearcher;

    @Override
    public String studentLogin(StudentLoginRequest studentLoginRequest, HttpServletRequest request) {
        String password = studentLoginRequest.getPassword();
        String username = studentLoginRequest.getUsername();
        
        if (StringUtils.isAnyBlank(password, username) ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码为空");
        }

        Student student = baseMapper.getStudentBySid(username);
        if (student == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "学生数据不存在");
        }

        // 判断密码是否正确
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!password.equals(student.getPassword())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "密码错误");
        }

        // 判断用户是否处于登陆状态
        if (student.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已在别处登陆");
        }

        // 判断用户ip是否在小黑屋中
        String ip = getIpAddr(request);
        LambdaQueryWrapper<BlackHouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlackHouse::getIp, ip);  // 判断小黑屋里有没有ip
        boolean exists = blackHouseMapper.exists(queryWrapper);
        if (exists) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您被封号，请联系开发人员");
        }

        // 生成token
        HashMap<String, String> jwt = new HashMap<>();
        jwt.put("username", username);
        jwt.put("id", student.getId().toString());

        String address = getIpAddress(ip);

        // 将本次登陆的ip和地址放入数据库中
        student.setAddressIp(ip);
        student.setAddressName(address);
        boolean save = this.updateById(student);

        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "ip插入数据库失败");
        }


        if (address == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取ip地址失败");
        }

        String token = JwtUtil.generateToken(jwt);

        return token;
    }

    /**
     * 获取用户登陆ip
     *
     * @param request 请求
     * @return 用户登陆地址和ip
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 本机访问
        if ("localhost".equalsIgnoreCase(ip) || "127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ip && ip.length() > 15) {
            if (ip.indexOf(",") > 15) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip;
    }

    private String getIpAddress(String ip) {
        // 根据ip地址获取到address
        IpInfo ipInfo = ip2regionSearcher.memorySearch(ip);
        String address = UNKNOWN;
        assert ipInfo != null;
        address = ipInfo.getAddress();

        return address;
    }
    
    
}




