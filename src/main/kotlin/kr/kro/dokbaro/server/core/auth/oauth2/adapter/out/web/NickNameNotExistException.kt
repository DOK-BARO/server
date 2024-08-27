package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class NickNameNotExistException : BadRequestException("nickname을 찾을 수 없습니다.")