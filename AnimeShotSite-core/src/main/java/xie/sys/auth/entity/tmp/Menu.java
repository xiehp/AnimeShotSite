package xie.sys.auth.entity.tmp;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class Menu implements Serializable {

	private static final long serialVersionUID = -7025252477399936159L;

	public Menu(String id, String name, String url, String menuIcon, String identity, Integer menuLevel) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.menuIcon = menuIcon;
		this.identity = identity;
		this.menuLevel = menuLevel;
	}

	private String id;
	private String name;
	private String url;
	private String menuIcon;
	private String identity;
	private Integer menuLevel;
	private List<Menu> children;

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Menu> getChildren() {
		if (children == null) {
			children = Lists.newArrayList();
		}
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public boolean isHasChildren() {
		return !getChildren().isEmpty();
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", url=" + url
				+ ", children=" + children + "]";
	}
}
