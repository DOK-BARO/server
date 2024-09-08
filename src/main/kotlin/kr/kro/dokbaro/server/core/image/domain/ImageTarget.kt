package kr.kro.dokbaro.server.core.image.domain

enum class ImageTarget(
	val path: String,
) {
	MEMBER_PROFILE("/user/profile/"),
	STUDY_GROUP_PROFILE("/studygroup/profile/"),
}