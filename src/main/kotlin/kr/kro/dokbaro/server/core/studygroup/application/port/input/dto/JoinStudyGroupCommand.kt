package kr.kro.dokbaro.server.core.studygroup.application.port.input.dto

import java.util.UUID

data class JoinStudyGroupCommand(
	val inviteCode: String,
	val participantAuthId: UUID,
)