package kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto

import java.util.UUID

data class StartSolvingQuizCommand(
	val authId: UUID,
	val quizId: Long,
)