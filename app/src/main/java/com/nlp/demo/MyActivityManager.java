package com.nlp.demo;

import android.app.Activity;
import android.content.Context;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description:
 * @author zdd
 * @date 2014-5-6 下午1:02:44
 * @remark
 */
public class MyActivityManager {
	private List<Activity> activitys = null;
	private static MyActivityManager instance;

	private MyActivityManager() {
		activitys = new LinkedList<Activity>();
	}

	public static MyActivityManager getInstance() {
		if (null == instance) {
			instance = new MyActivityManager();
		}
		return instance;
	}

	 //添加Activity到容器中之后,每个页面结束一定要调用removeActivity(),否则会导致每个finish的activity有到gc的根节点，导致finish的activity无法释放内存（尤其是有图片的界面）。
	public void addActivity(Activity activity) {
		if (activitys != null) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys=new LinkedList<Activity>();
			activitys.add(activity);
		}

	}

	public void removeActivity(Activity activity){
		if (activitys != null && activitys.size() > 0){
			activitys.remove(activity);
		}
	}

	public void clearAll() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
	}

	public void exit() {
		clearAll();
		System.exit(0);
	}
}
