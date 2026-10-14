package com.daleen.freelanceflow.domain.repository

interface SyncRepository {
    /**
     * Pushes every local row to Supabase, then pulls every remote row for the
     * signed-in user back into Room. No-ops successfully if signed out.
     *
     * Deletions are not yet tracked (push/pull only add or update rows), so a
     * row deleted on one device currently persists remotely/on other devices
     * until deleted there too — a documented limitation, not a correctness bug.
     */
    suspend fun syncAll(): Result<Unit>
}
