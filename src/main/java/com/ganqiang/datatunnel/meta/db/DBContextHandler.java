package com.ganqiang.datatunnel.meta.db;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.ganqiang.datatunnel.conf.Pool;
import com.ganqiang.datatunnel.core.Constants;
import com.ganqiang.datatunnel.meta.Visitable;
import com.ganqiang.datatunnel.meta.Visitor;

public class DBContextHandler implements Visitable {

	private Pool pool;

	public DBContextHandler(Pool pool){
		this.pool = pool;
	}

	public void init() {
		PoolProperties p = new PoolProperties();
		p.setUrl(pool.getUrl());
		String driver = "com.mysql.jdbc.Driver";
	 if(pool.getType().contains("Oracle")){
		 driver = "oracle.jdbc.driver.OracleDriver";
		}
		p.setDriverClassName(driver);
		p.setUsername(pool.getUserName());
		p.setPassword(pool.getPassword());
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setLogValidationErrors(true);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(200);
		p.setInitialSize(pool.getPoolSize());
		p.setMaxWait(10000);
		// p.setRemoveAbandonedTimeout(120);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(false);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		DataSource dataSource = new DataSource();
		dataSource.setPoolProperties(p);
		Constants.db_datasource_map.put(pool.getId(), dataSource);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitDBContext(this);
	}

}
