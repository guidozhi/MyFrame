package com.neo.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.neo.model.User;
import com.neo.enums.UserSexEnum;
import com.neo.mapper.plan.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class User1MapperTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void testInsert() throws Exception {
		userMapper.insert(new User("aammxd", "a123456", UserSexEnum.MAN));
		userMapper.insert(new User("bbmmxd", "b123456", UserSexEnum.WOMAN));
		userMapper.insert(new User("ccmmxd", "b123456", UserSexEnum.WOMAN));

//		Assert.assertEquals(3, userMapper.getAll().size());
	}

	@Test
	public void testQuery() throws Exception {
		List<User> users = userMapper.getAll();
		if(users==null || users.size()==0){
			System.out.println("is null");
		}else{
			System.out.println(users.size());
		}
	}
	
	
	@Test
	public void testUpdate() throws Exception {
		User user = userMapper.getOne(6l);
		System.out.println(user.toString());
		user.setNickName("neo");
		userMapper.update(user);
		Assert.assertTrue(("neo".equals(userMapper.getOne(6l).getNickName())));
	}

}