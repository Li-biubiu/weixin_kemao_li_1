package org.weixin.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weixin.commons.domain.User;

// 此时Spring Data JPA会自动创建此接口的实现类，并排产生实例放入容器中
//	extends JpaRepository : 表示继承Spring Data JPA提供的基本CRUD接口，基本上大部分的操作都已经工体
//	<User , String> : 第一个参数表示哪个类的数据(对应哪个表) ， 第二个参数则表示里面主键的数据类型(@id注解)
@Repository	
public interface UserRepository extends JpaRepository<User, String> {

	// 不需要实现吃饭饭， Spring会自动实现(利用动态代理技术实现)
	// 最终生成的SQL语句 ：select * from li_user where open_id = ?
	// 并且会自动把查询结果转换为User类的实例
	User findByOpenId(String openId);
	
}
