package com.darcy.kotlin.server.demowebsocket.exception

class ConversationException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val CONVERSATION_NOT_EXIST = ConversationException(701, "会话不存在")
        val CONVERSATION_CREATE_FAILED = ConversationException(702, "会话创建失败")
        val CONVERSATION_UPDATE_FAILED = ConversationException(703, "会话更新失败")
        val CONVERSATION_DELETE_FAILED = ConversationException(704, "会话删除失败")
        val CONVERSATION_TYPE_ERROR = ConversationException(705, "会话类型错误")
    }
}