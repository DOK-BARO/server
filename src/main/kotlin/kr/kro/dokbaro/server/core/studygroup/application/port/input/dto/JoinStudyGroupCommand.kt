package kr.kro.dokbaro.server.core.studygroup.application.port.input.dto

data class JoinStudyGroupCommand(
	val inviteCode: String,
	val memberId: Long,
	val memberNickname: String,
)