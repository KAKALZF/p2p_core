package com.xmg.springboot.p2p.base.Query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SystemDictionaryQueryObject extends QueryObject {
	private String keyword;
	private int state;
	private Long parentId;
}
