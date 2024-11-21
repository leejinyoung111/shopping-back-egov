package egovframework.example.sample.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

@Mapper("memberMapper")
public interface MemberMapper {
	public int select_membercount() throws Exception;
	public String selectName() throws Exception;
	

}
