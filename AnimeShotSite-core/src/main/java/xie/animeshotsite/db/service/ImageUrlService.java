package xie.animeshotsite.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yjysh.framework.base.repository.BaseRepository;
import com.yjysh.framework.base.service.BaseService;

import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.repository.ImageUrlDao;

@Service
public class ImageUrlService extends BaseService<ImageUrl, String> {

	@Autowired
	private ImageUrlDao imageUrlDao;

	@Override
	public BaseRepository<ImageUrl, String> getBaseRepository() {
		return imageUrlDao;
	}

	public ImageUrl saveImageInfo(String rootPath, String detailPath, String name, String tietukuImageUrlId, String tietukuImageUrlPrefix) {
		ImageUrl imageUrl = new ImageUrl();
		imageUrl.setLocalRootPath(rootPath);
		imageUrl.setLocalDetailPath(detailPath);
		imageUrl.setLocalFileName(name);

		imageUrl.setTietukuUrlId(tietukuImageUrlId);
		imageUrl.setTietukuUrlPrefix(tietukuImageUrlPrefix);

		return imageUrlDao.save(imageUrl);
	}

}
