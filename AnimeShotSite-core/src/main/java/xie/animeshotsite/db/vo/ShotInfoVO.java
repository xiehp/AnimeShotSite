package xie.animeshotsite.db.vo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.tietuku.entity.util.TietukuUtils;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.common.date.DateUtil;

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
