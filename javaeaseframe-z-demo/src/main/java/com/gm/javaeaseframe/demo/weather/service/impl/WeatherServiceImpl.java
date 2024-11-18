/**
* 文件：WeatherServiceImpl.java
* 版本：1.0.0
* 日期：
* Copyright &reg; 
* All right reserved.
*/

package com.gm.javaeaseframe.demo.weather.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.javaeaseframe.core.context.service.impl.AbstractCRUDServiceImpl;
import com.gm.javaeaseframe.demo.weather.dao.WeatherDao;
import com.gm.javaeaseframe.demo.weather.model.WeatherEntity;
import com.gm.javaeaseframe.demo.weather.model.WeatherQuery;
import com.gm.javaeaseframe.demo.weather.service.TestService;
import com.gm.javaeaseframe.demo.weather.service.WeatherService;

/**
 * <p>Title: 城市天气信息</p>
 * <p>Description: WeatherServiceImpl service接口 </p>
 * <p>Copyright: Copyright &reg;  </p>
 * <p>Company: </p>
 * @author 
 * @version 1.0.0
 */
@Service("weatherService")
public class WeatherServiceImpl extends AbstractCRUDServiceImpl<WeatherDao,WeatherEntity,Long> implements WeatherService {

	@Autowired
	private TestService testService;
	
	public void start() {
		log.info("开始加载天气信息...");
	}

	public void stop() {
		log.info("开始关闭天气信息...");
	}
	
    @Override
    public WeatherEntity getTodayByArea(Long areaId) {
        if (areaId == null || areaId.longValue() == 0) {
            areaId = 5101L;//默认显示成都天气
        }
        WeatherQuery params = new WeatherQuery();
        params.setAreaId(areaId);
        params.setDate(Integer.parseInt(DateFormatUtils.format(new Date(), "yyyyMMdd")));
        List<WeatherEntity> list = dao.getList(params);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    @Override
    public List<WeatherEntity> findForecastsByArea(Long areaId) {
        DateTimeFormatter dayfmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate local = LocalDate.now();
        String startDate = local.format(dayfmt);
        String endDate = local.plusDays(7).format(dayfmt);
        return this.findForecastsByArea(areaId, startDate, endDate);
    }
    
    @Override
    public List<WeatherEntity> findForecastsByArea(Long areaId, String startDate, String endDate) {
        if (areaId == null || areaId.longValue() == 0) {
            areaId = 5101L;//默认显示成都天气
        }
        WeatherQuery params = new WeatherQuery();
        params.setAreaId(areaId);
        if (StringUtils.isNotEmpty(startDate)) {
            params.setDateStart(Integer.parseInt(startDate));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            params.setDateEnd(Integer.parseInt(endDate));
        }
        return dao.getList(params);
    }

    @Override
    public int saveOrUpdate(WeatherEntity entity) {
        WeatherEntity params = new WeatherEntity();
        params.setAreaId(entity.getAreaId());
        params.setDate(entity.getDate());
        List<WeatherEntity> list = this.dao.getList(params);
        if (list != null && list.size() > 0) {
            WeatherEntity oldEntity = list.get(list.size() - 1);
            entity.setId(oldEntity.getId());
            return this.dao.update(entity);
        } else {
            return this.dao.insert(entity);
        }
    }
    @Override
    public int saveOrUpdate(List<WeatherEntity> weatherList) {
        int count = 0;
        for (WeatherEntity entity : weatherList) {
            count += this.saveOrUpdate(entity);
        }
        return count;
    }
    
    @Override
	public void save() {
    	System.out.println("testTransactionTx");
    	try {
			testService.save();
		} catch (Exception e) {
			testService.search();
		}
	}

	@Override
	public void search() {
		System.out.println("testReadOnlyTransaction");
		try {
			testService.save();
		} catch (Exception e) {
			testService.search();
		}
	}

	@Override
	public void search2() {
		System.out.println("testSupportsTransaction");
		try {
			testService.save();
		} catch (Exception e) {
			testService.search();
		}
	}

	@Override
	public void searchNonTx() {
		System.out.println("testNotSupportedTransaction");
		try {
			testService.save();
		} catch (Exception e) {
			testService.search();
		}
	}
	
}