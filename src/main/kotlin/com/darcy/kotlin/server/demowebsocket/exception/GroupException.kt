package com.darcy.kotlin.server.demowebsocket.exception

/**
 * 群组相关异常
 * 901-999
 */
class GroupException (
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    companion object {
        val GROUP_NOT_EXIST = GroupException(901, "群组不存在")
        val GROUP_INFO_NOT_CHANGED = GroupException(902, "群组信息未发生变化，无需修改")
        val GROUP_DELETE_FAILED = GroupException(903, "群组删除失败")
        val GROUP_FULL = GroupException(904, "群组已满")
    }
}