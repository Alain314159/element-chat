/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.app.features.home.room.list.usecase

import im.vector.app.core.di.ActiveSessionHolder
import org.matrix.android.sdk.api.session.events.model.EventType
import org.matrix.android.sdk.api.session.getRoom
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent
import javax.inject.Inject

class GetLatestPreviewableEventUseCase @Inject constructor(
        private val sessionHolder: ActiveSessionHolder,
) {

    fun execute(roomId: String): TimelineEvent? {
        val room = sessionHolder.getSafeActiveSession()?.getRoom(roomId) ?: return null
        val roomSummary = room.roomSummary() ?: return null
        return roomSummary.latestPreviewableEvent
    }

    private fun getCallEvent(roomSummary: RoomSummary): TimelineEvent? {
        return roomSummary.latestPreviewableEvent
                ?.takeIf { it.root.getClearType() == EventType.CALL_INVITE }
    }
}
