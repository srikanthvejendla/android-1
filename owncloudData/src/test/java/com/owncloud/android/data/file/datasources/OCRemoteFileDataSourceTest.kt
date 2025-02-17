/**
 * ownCloud Android client application
 *
 * @author Abel García de Prada
 * Copyright (C) 2020 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.data.file.datasources

import com.owncloud.android.data.ClientManager
import com.owncloud.android.data.files.datasources.implementation.OCRemoteFileDataSource
import com.owncloud.android.lib.common.operations.RemoteOperationResult
import com.owncloud.android.lib.resources.files.services.implementation.OCFileService
import com.owncloud.android.testutil.OC_ACCOUNT_NAME
import com.owncloud.android.testutil.OC_FOLDER
import com.owncloud.android.testutil.OC_SECURE_SERVER_INFO_BASIC_AUTH
import com.owncloud.android.utils.createRemoteOperationResultMock
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class OCRemoteFileDataSourceTest {
    private lateinit var ocRemoteFileDataSource: OCRemoteFileDataSource

    private val clientManager: ClientManager = mockk(relaxed = true)
    private val ocFileService: OCFileService = mockk()

    @Before
    fun init() {
        every { clientManager.getFileService(any()) } returns ocFileService

        ocRemoteFileDataSource = OCRemoteFileDataSource(clientManager)
    }

    @Test
    fun checkPathExistenceTrue() {
        val checkPathExistenceRemoteResult: RemoteOperationResult<Boolean> =
            createRemoteOperationResultMock(data = true, isSuccess = true)

        every {
            ocFileService.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true)
        } returns checkPathExistenceRemoteResult

        val checkPathExistence = ocRemoteFileDataSource.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true, OC_ACCOUNT_NAME, null)

        assertNotNull(checkPathExistence)
        assertEquals(checkPathExistenceRemoteResult.data, checkPathExistence)

        verify { ocFileService.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true) }
    }

    @Test
    fun checkPathExistenceFalse() {
        val checkPathExistenceRemoteResult: RemoteOperationResult<Boolean> =
            createRemoteOperationResultMock(data = false, isSuccess = true)

        every {
            ocFileService.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true)
        } returns checkPathExistenceRemoteResult

        val checkPathExistence = ocRemoteFileDataSource.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true, OC_ACCOUNT_NAME, null)

        assertNotNull(checkPathExistence)
        assertEquals(checkPathExistenceRemoteResult.data, checkPathExistence)

        verify { ocFileService.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true) }
    }

    @Test(expected = Exception::class)
    fun checkPathExistenceException() {
        every {
            ocFileService.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true)
        } throws Exception()

        ocRemoteFileDataSource.checkPathExistence(OC_SECURE_SERVER_INFO_BASIC_AUTH.baseUrl, true, OC_ACCOUNT_NAME, null)
    }

    @Test
    fun createFolderSuccess() {
        val createFolderRemoteResult: RemoteOperationResult<Unit> =
            createRemoteOperationResultMock(data = Unit, isSuccess = true)

        every {
            ocFileService.createFolder(remotePath = OC_FOLDER.remotePath, createFullPath = false, isChunkFolder = false)
        } returns createFolderRemoteResult

        val createFolderResult = ocRemoteFileDataSource.createFolder(OC_FOLDER.remotePath, false, false, OC_ACCOUNT_NAME, null)

        assertNotNull(createFolderResult)
        assertEquals(createFolderRemoteResult.data, createFolderResult)

        verify { ocFileService.createFolder(any(), any(), any()) }
    }

    @Test(expected = Exception::class)
    fun createFolderException() {
        every {
            ocFileService.createFolder(OC_FOLDER.remotePath, false, false)
        } throws Exception()

        ocRemoteFileDataSource.createFolder(OC_FOLDER.remotePath, false, false, OC_ACCOUNT_NAME, null)
    }
}
