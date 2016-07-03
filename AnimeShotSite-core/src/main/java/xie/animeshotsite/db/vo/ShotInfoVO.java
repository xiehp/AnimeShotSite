package xie.animeshotsite.db.vo;

import xie.animeshotsite.db.entity.ShotInfo;

public class ShotInfoVO extends ShotInfo {

	private static final long serialVersionUID = 6049646624290779878L;

	private String tietukuOUrlChangeDomain;

	public String getTietukuOUrlChangeDomain() {
		return tietukuOUrlChangeDomain;
	}

	public void setTietukuOUrlChangeDomain(String tietukuOUrlChangeDomain) {
		this.tietukuOUrlChangeDomain = tietukuOUrlChangeDomain;
	}
}
