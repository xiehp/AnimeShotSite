package xie.sys.dictionary.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import xie.common.utils.DictionaryLoader;
import xie.sys.dictionary.entity.PublicDictionary;

public class PublicDictionaryLoader {

	public static Map<Integer, String> getIntMap(final String typeId) {
		final Map<String, String> tmpMap = DictionaryLoader.getInstance().getMap(typeId);
		final Map<Integer, String> newMap = new LinkedHashMap<Integer, String>();
		for (Map.Entry<String, String> entry : tmpMap.entrySet()) {
			newMap.put(Integer.valueOf(entry.getKey()), entry.getValue());
		}
		return newMap;
	}

	public static Map<Integer, PublicDictionary> getEntityIntMap(final String typeId) {
		final Map<String, PublicDictionary> tmpMap = DictionaryLoader.getInstance().getEntityMap(typeId);
		final Map<Integer, PublicDictionary> newMap = new LinkedHashMap<Integer, PublicDictionary>();
		for (Map.Entry<String, PublicDictionary> entry : tmpMap.entrySet()) {
			newMap.put(Integer.valueOf(entry.getKey()), entry.getValue());
		}
		return newMap;
	}

	public static Map<Integer, String> getValidIntMap(final String typeId) {
		final Map<String, String> tmpMap = DictionaryLoader.getInstance().geValidtMap(typeId);
		final Map<Integer, String> newMap = new LinkedHashMap<Integer, String>();
		for (Map.Entry<String, String> entry : tmpMap.entrySet()) {
			newMap.put(Integer.valueOf(entry.getKey()), entry.getValue());
		}
		return newMap;
	}

	public static Map<Integer, PublicDictionary> getValidEntityIntMap(final String typeId) {
		final Map<String, PublicDictionary> tmpMap = DictionaryLoader.getInstance().getValidMapEntity(typeId);
		final Map<Integer, PublicDictionary> newMap = new LinkedHashMap<Integer, PublicDictionary>();
		for (Map.Entry<String, PublicDictionary> entry : tmpMap.entrySet()) {
			newMap.put(Integer.valueOf(entry.getKey()), entry.getValue());
		}
		return newMap;
	}

	public static Map<String, String> getMap(final String typeId) {
		return DictionaryLoader.getInstance().getMap(typeId);
	}

	public static Map<String, PublicDictionary> getEntityMap(final String typeId) {
		return DictionaryLoader.getInstance().getEntityMap(typeId);
	}

	public static Map<String, String> getValidMap(final String typeId) {
		return DictionaryLoader.getInstance().geValidtMap(typeId);
	}

	public static Map<String, PublicDictionary> getValidEntityMap(final String typeId) {
		return DictionaryLoader.getInstance().getValidMapEntity(typeId);
	}

	public static String getValue(final String typeId, final String code) {
		Map<String, String> map = PublicDictionaryLoader.getMap(typeId);
		return map.get(code);
	}

	public static String getValue(final String typeId, final Integer code) {
		Map<Integer, String> map = PublicDictionaryLoader.getIntMap(typeId);
		return map.get(code);
	}

	public static void reload() {
		DictionaryLoader.reload();
	}

}
