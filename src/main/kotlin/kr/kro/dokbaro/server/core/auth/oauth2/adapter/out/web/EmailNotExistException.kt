package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class EmailNotExistException : BadRequestException("이메일이 존재하지 않습니다")