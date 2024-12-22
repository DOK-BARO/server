package kr.kro.dokbaro.server.core.studygroup.application.port.input.dto

data class WithdrawStudyGroupMemberCommand(
	val studyGroupId: Long,
	val memberId: Long,
)