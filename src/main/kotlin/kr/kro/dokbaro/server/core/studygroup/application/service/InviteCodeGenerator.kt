package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.studygroup.domain.InviteCode

interface InviteCodeGenerator {
	fun generate(): InviteCode
}