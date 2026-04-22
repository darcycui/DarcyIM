package com.darcy.kotlin.server.demowebsocket.http.service

import com.darcy.kotlin.server.demowebsocket.domain.table.group.GroupInvite
import com.darcy.kotlin.server.demowebsocket.exception.GroupException
import com.darcy.kotlin.server.demowebsocket.exception.user.UserException
import com.darcy.kotlin.server.demowebsocket.http.repository.GroupInviteRepository
import com.darcy.kotlin.server.demowebsocket.http.repository.GroupRepository
import com.darcy.kotlin.server.demowebsocket.utils.IdGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupInviteService @Autowired constructor(
    private val groupInviteRepository: GroupInviteRepository,
    private val groupRepository: GroupRepository,
    private val userService: UserService,
    private val friendshipService: FriendshipService,
    private val idGenerator: IdGenerator
) {
    fun createGroupInvite(inviterId: Long, inviteeId: Long, id: Long): GroupInvite {
        val invitor = userService.getUserById(inviterId)
        val invitee = userService.getUserById(inviteeId)
        val isFriend = friendshipService.isFriend(inviterId, inviteeId)
        if (!isFriend) {
            throw UserException.FRIENDSHIP_NOT_EXIST
        }
        val group = groupRepository.findById(id).orElse(null) ?: throw GroupException.GROUP_NOT_EXIST
        val groupInvite = GroupInvite(
            inviter = invitor,
            invitee = invitee,
            group = group,
            status = GroupInvite.InviteStatus.ACCEPTED, // 默认接受群邀请
        )
        return groupInviteRepository.save(groupInvite)
    }

    fun queryGroupInvitesByFromUser(userId: Long): List<GroupInvite> {
        return groupInviteRepository.findByInviterId(userId)
    }

    fun queryGroupInvitesByToUser(userId: Long): List<GroupInvite> {
        return groupInviteRepository.findByInviteeId(userId)
    }
}