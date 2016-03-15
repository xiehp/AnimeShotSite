package xie.animeshotsite.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "image_url")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ImageUrl extends BaseImageUrl {

	private static final long serialVersionUID = 6426982810483721875L;

}
