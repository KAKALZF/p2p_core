package com.xmg.springboot.p2p.base.service;

import java.util.List;

import com.xmg.springboot.p2p.base.Query.UserfileQueryObject;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.Userfile;
import com.xmg.springboot.p2p.base.page.PageResult;

/**
 * 用户风控材料服务
 * @author 1
 *
 */
public interface IUserFileService {
	/**
	 * 申请一个风控材料
	 * @param image
	 */
	void apply(String image, Long userid);

	/**
	 * 列出指定用户没有选择风控分类的材料风控列表
	 * @param id
	 * @param selected 
	 * @return
	 */
	List<Userfile> listSelectTypeUserFiles(Long id, boolean selected);

	void batchChoiceType(Long[] id, Long[] fileType);

	/**
	 * 后台审核风控材料分页查询
	 */

	PageResult query(UserfileQueryObject qo);

	void audit(Long id, int state, String remark, int score, Logininfo current);

	List<Userfile> queryForList(UserfileQueryObject qo);
}
