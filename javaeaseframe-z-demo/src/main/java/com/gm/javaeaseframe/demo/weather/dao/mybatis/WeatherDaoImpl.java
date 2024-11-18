/**
* 文件：WeatherDaoImpl.java
* 版本：1.0.0
* 日期：
* Copyright &reg; 
* All right reserved.
*/

package com.gm.javaeaseframe.demo.weather.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.gm.javaeaseframe.core.context.dao.mybatis.BaseCRUDDaoMybatis;
import com.gm.javaeaseframe.demo.weather.dao.WeatherDao;
import com.gm.javaeaseframe.demo.weather.model.WeatherEntity;

/**
 * <p>Title: 城市天气信息</p>
 * <p>Description: WeatherDaoImpl DAO接口 </p>
 * <p>Copyright: Copyright &reg;  </p>
 * <p>Company: </p>
 * @author 
 * @version 1.0.0
 */
@Repository("weatherDao")
public class WeatherDaoImpl extends BaseCRUDDaoMybatis<WeatherEntity,Long> implements WeatherDao {

}