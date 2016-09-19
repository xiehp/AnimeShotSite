package xie.other.ma.db.service;

import java.io.Serializable;

import xie.base.entity.IdEntity;
import xie.base.service.BaseService;
import xie.other.ma.db.entity.CommonRecordDetail;

public abstract class CommonRecordDetailService<M extends IdEntity, ID extends Serializable> extends BaseService<M, ID> {

	public abstract CommonRecordDetail getDetail(ID id);

}
