package kr.kro.dokbaro.server.core.member.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.UnauthorizedException

class NotFoundCertificationMemberException : UnauthorizedException("member not found")