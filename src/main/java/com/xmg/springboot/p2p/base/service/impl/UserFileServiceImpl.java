package com.xmg.springboot.p2p.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.Query.UserfileQueryObject;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.SystemDictionaryItem;
import com.xmg.springboot.p2p.base.domain.Userfile;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.mapper.UserfileMapper;
import com.xmg.springboot.p2p.base.mapper.UserinfoMapper;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.base.service.IUserFileService;
import com.xmg.springboot.p2p.base.service.IUserinfoService;

@Service
public class UserFileServiceImpl implements IUserFileService {
	@Autowired
	private UserfileMapper userfileMapper;
	@Autowired
	private IUserinfoService userinfoService;

	@Override
	public void apply(String image, Long userid) {
		Userfile uf = new Userfile();
		Logininfo li = new Logininfo();
		li.setId(userid);
		uf.setApplier(li);
		uf.setApplyTime(new Date());
		uf.setImage(image);
		uf.setState(Userfile.STATE_NORMAL);
		userfileMapper.insert(uf);
	}

	@Override
	public List<Userfile> listSelectTypeUserFiles(Long id, boolean selected) {
		// TODO Auto-generated method stub
		return userfileMapper.listSelectTypeUserFiles(id, selected);
	}

	@Override
	public void batchChoiceType(Long[] ids, Long[] fileTypes) {
		// TODO Auto-generated method stub
		for (int i = 0; i < ids.length; i++) {
			Userfile userfile = userfileMapper.selectByPrimaryKey(ids[i]);
			SystemDictionaryItem item = new SystemDictionaryItem();
			item.setId(fileTypes[i]);
			userfile.setFileType(item);
			userfileMapper.updateByPrimaryKey(userfile);
		}
	}

	@Override
	public PageResult query(UserfileQueryObject qo) {
		int count = userfileMapper.queryForCount(qo);
		if (count > 0) {
			List<Userfile> list = userfileMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	/**
	 * 风控材料审核
	 */
	@Override
	public void audit(Long id, int state, String remark, int score,
			Logininfo current) {
		//判断
		Userfile file = userfileMapper.selectByPrimaryKey(id);
		if (file != null && file.getFileType() != null
				&& file.getState() == Userfile.STATE_NORMAL) {

			//处于待审核状态,有分类
			//设置通用的审核内容
			file.setAuditTime(new Date());
			file.setAuditor(current);
			file.setRemark(remark);
			file.setScore(score);
			file.setState(state);
			//如果审核通过
			//把分数累加
			if (state == Userfile.STATE_PASS) {
				Userinfo userinfo = userinfoService.get(file.getApplier()
						.getId());
				userinfo.setScore(userinfo.getScore() + score);
				userinfoService.update(userinfo);
			}
			userfileMapper.updateByPrimaryKey(file);
		}

	}

	@Override
	public List<Userfile> queryForList(UserfileQueryObject qo) {
		return userfileMapper.query(qo);
	}

}
