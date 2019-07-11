package com.park61.teacherhelper.module.home.bean;


public class CourseCombine {
	private CourseSectionBean courseBeanLeft;
	private CourseSectionBean courseBeanRight;

	public CourseCombine() {
	}

	public CourseCombine(CourseSectionBean courseBeanLeft, CourseSectionBean courseBeanRight) {
		this.setCourseBeanLeft(courseBeanLeft);
		this.setCourseBeanRight(courseBeanRight);
	}

	public CourseSectionBean getCourseBeanLeft() {
		return courseBeanLeft;
	}

	public void setCourseBeanLeft(CourseSectionBean courseBeanLeft) {
		this.courseBeanLeft = courseBeanLeft;
	}

	public CourseSectionBean getCourseBeanRight() {
		return courseBeanRight;
	}

	public void setCourseBeanRight(CourseSectionBean courseBeanRight) {
		this.courseBeanRight = courseBeanRight;
	}
}
