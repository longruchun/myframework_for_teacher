package com.hx.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.javafx.collections.MappingChange.Map;



public class system_servlet {
    public static HashMap<String, my_servlet> map= new HashMap<String,my_servlet>(); 
    
    public void reading(String config) {
    	//��dom4j�������������ļ��е�servletȫ����ȡ��list��
    	//��ȡ�Ĵ���
   	 try {
			if (config == null || config.isEmpty()) {
				throw new Exception("�Զ����ܵ������ļ�Ϊ��");
			}
			 
			java.io.InputStream is = this.getClass().getResourceAsStream(config);
			
			Document doc = new SAXReader().read(is);
			Element root = doc.getRootElement();
			Iterator<Element> it = root.elements("bean").iterator();
			
			
			while(it.hasNext()) {
				Element bean = it.next();
				
				my_servlet m_s=new my_servlet();
				m_s.setId(bean.attributeValue("id"));
				m_s.setClazz(bean.attributeValue("class"));
				
				map.put(m_s.getId(),m_s);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
