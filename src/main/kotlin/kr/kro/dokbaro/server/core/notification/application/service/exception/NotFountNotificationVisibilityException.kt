package kr.kro.dokbaro.server.core.notification.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFountNotificationVisibilityException : NotFoundException("Notification not found")