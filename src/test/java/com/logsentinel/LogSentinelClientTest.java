package com.logsentinel;

import com.logsentinel.client.model.*;
import com.logsentinel.merkletree.utils.ArrayUtils;
import com.logsentinel.merkletree.utils.CryptoUtils;
import com.logsentinel.merkletree.utils.TreeUtils;
import com.logsentinel.merkletree.verification.ConsistencyProofVerification;
import com.logsentinel.merkletree.verification.InclusionProofVerification;
import com.logsentinel.util.StringUtil;
import com.logsentinel.util.TimeStampUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

@SuppressWarnings("SpellCheckingInspection")
public class LogSentinelClientTest {
    final String applicationId = "ae37f8c0-7f38-11e8-bf35-cbf6b8eea46f";
    final String organizationId = "ae1c3360-7f38-11e8-bf35-cbf6b8eea46f";
    final String secret = "846b72776182fe44a9e31dc009f9d97989b64e251d323acff43dc3d665e6ac15";

    // @Test
    public void getVerificationActions() {
        // Test the audit log verification controller

        LogSentinelClientBuilder builder = LogSentinelClientBuilder
                .create(applicationId, organizationId, secret);
        builder.setBasePath("http://localhost:8080");

        LogSentinelClient client = builder.build();

        // Hash of an audit log entry that was included in this application's log
        String hash1 = "0qHnEuGmu5I5vBIURvcjkTDw3LF_t_BQLUWRvoutPCSaCU1-j9lafQ8A_qbJFfkiUYU1fHtz9OyCwWP_XUjHbw==";

        // Hash of an audit log entry that was included in this application's log at a later stage
        String hash2 = "aGpNDMW5vIJ1Mbe9fb-SSCyQoH6ZFigFCJ2ZvGIUn2pIWF00IaOzRNTpfckvwF7cmyXLJFnM3-9VOUNVKyLc9g==";

        // Certificate used by LogSentinel for time stamping
        String logSentinelTsCert = "MIICzjCCAbagAwIBAgIEWPIrGjANBgkqhkiG9w0BAQ0FADAbMRkwFwYDVQQDDBBhdWRpdGxvZy10c2Eta2V5MB4XDTE3MDQxNTE0MTcyM1oXDTIyMDQxNTE0MTcyM1owGzEZMBcGA1UEAwwQYXVkaXRsb2ctdHNhLWtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJ25X+ady+Af1K1AcNGJAVIJY2qR47DXF9gHFSXkV4fEuHvnEPzCozXNNpgwHbVVEJs0mUsaEG+MukZtJ1WWJha/qFYB9eMotYucVwUt1YulUbAJbWLb99oMJ8KyHWJtVFTfHJL+j3DScLzBQ0QglG7RiOc7gQohBnamtwe7ayIIOa/BJREYtK5o9rBLSddGeXZjoJzSrXARvaPnDolHuqK/eseFeLWJN0IvykCK/On3FCeWVtrzPlIrm1NVaVcyrK1x3j5P5I9pYZ2b32446FyXsQZlr0TvKvTbs5eKNGJ4270YVqYmSM6aZZH+x5X3oNujRk9q6OKepPv4vr9dRDcCAwEAAaMaMBgwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwgwDQYJKoZIhvcNAQENBQADggEBAJVn4p9ftwXqOsGyLuZYkCA2PxsSrMQXbrIk4geWsD7U9ZV/2yEuOKWKRmBna8i+d/g+UmNKo2EVYUaVDb+FxXbJswGSh/WHsCmVKn62iR4JldbNZHgQEBXRndxjfzigBqzbtPTIx7ivk7RyX2eSv4sA4E/Wb3Pshiqt5n9TZqDSeCUsshKOpE6WBdDyApsjDiJ/W/xhy9x9RhpAJdni+W0NgGQK/a96fxoKB75zoKqD1gnmr0WojQcDFFwMxLnVD0j0m9UECWJxI/67RkQEvequD13nZShseH4cF8hGLYF/rK0eDbjleBpAG4GBEpVY2jdVKU3KaHMdOFD+kWCT2+0=";

        // A historical Merkle Tree Head (MTH); retrieved from Ethereum, Twitter, etc.
        String historicalMth = "PBPLkDuEYgo-C55y59egFWFm9FUpDu4xUf60uw4rIpI=";

        try {
            // Retrieve basic log information
            LogInfo logInfo = client.getVerificationActions().getMerkleTreeInfo();

            Assert.assertNotNull(logInfo);
            Assert.assertEquals("2.16.840.1.101.3.4.2.3", logInfo.getLeafHashAlgorithmOid());
            Assert.assertEquals("2.16.840.1.101.3.4.2.1", logInfo.getHashAlgorithmOid());
            Assert.assertEquals("1.2.840.113549.1.1.13", logInfo.getTimestampAlgorithmOid());

            // Retrieve the latest Merkle Tree Head (MTH)
            TreeHead treeHead = client.getVerificationActions().getLatestTreeHead(applicationId);

            Assert.assertNotNull(treeHead);
            Assert.assertTrue(treeHead.getTreeSize() > 0);
            Assert.assertNotEquals(treeHead.getTimestampToken(), "");
            Assert.assertNotEquals(treeHead.getRootHash(), "");

            // Verify the time stamp token over the latest Merkle Tree Head (MTH)
            Assert.assertTrue(TimeStampUtil.verifyMthTimeStamp(treeHead.getRootHash(), treeHead.getTimestampToken(),
                    logSentinelTsCert));

            // List of standalone hashes that are going to be verified for inclusion
            List<String> entriesForVerification = new ArrayList<>();

            // Retrieve all entries between two hashes
            List<AuditLogEntry> logEntries = client.getVerificationActions().getEntriesBetweenHashes(hash1,
                    hash2, applicationId);

            Assert.assertNotNull(logEntries);
            Assert.assertTrue(logEntries.size() > 0);

            for (AuditLogEntry logEntry : logEntries) {
                Assert.assertNotNull(logEntry.getHash());
                Assert.assertNotNull(logEntry.getTimestampTime());
                Assert.assertNotNull(logEntry.getId());
            }

            // Perform a verification of the hash chain using the retrieved entries
            boolean logChainingVerificationError = false;

            for (int i = 0; i <= logEntries.size() - 1; i++) {
                String prevEntryHash = i > 0 ? logEntries.get(i - 1).getHash() : "";

                // Encode the given audit log entry using a specific format
                String expectedEntryStandaloneData = logEntries.get(i).getHashableContent();

                // Calculate the expected standalone hash of the given audit log entry (and not the hash that includes
                // the hash of the previuos audit log entry)
                String expectedEntryStandaloneHash = Base64.getUrlEncoder().encodeToString(
                        DigestUtils.sha512(expectedEntryStandaloneData));

                // Calculate the expected hash of the given audit log entry as part of the hash chain
                String expectedEntryHash =  Base64.getUrlEncoder().encodeToString(DigestUtils.sha512(
                        expectedEntryStandaloneData + prevEntryHash));

                // Retrieve the standalone hash of the given audit log entry from the server
                String entryStandaloneHash = client.getHashActions().getHashUsingPOST(applicationId,
                        UUID.fromString(logEntries.get(i).getId()));

                // The calculated expected standalone hash of the given audit log entry should match the one on the
                // server
                Assert.assertEquals(expectedEntryStandaloneHash, entryStandaloneHash);

                // The calculated expected hash of the given audit log entry should match the retrieved one
                if (!expectedEntryHash.equals(logEntries.get(i).getHash())) {
                    logChainingVerificationError = true;
                }

                // Add the standalone hash of the given audit log entry for Merkle inclusion verification
                entriesForVerification.add(expectedEntryStandaloneHash);
            }

            // If there were no errors during verification (e.g. logChainingVerificationError=false), the hash chain is
            // valid
            Assert.assertFalse(logChainingVerificationError);

            // Verify the inclusion of the audit log entries identified by their standalone hashes in the previously
            // generated list
            for (String entryForVerification : entriesForVerification) {
                // Retrieve the inclusion proof data and the latest MTH from the server
                InclusionProof inclusionProof = client.getVerificationActions().getInclusionProof(
                        entryForVerification,
                        applicationId);

                Assert.assertNotNull(inclusionProof);
                Assert.assertEquals(entryForVerification,
                        inclusionProof.getHash());
                Assert.assertTrue(inclusionProof.getIndex() >= 0);
                Assert.assertTrue(inclusionProof.getPath().size() > 0);
                Assert.assertTrue(inclusionProof.getTreeSize() > 0);
                Assert.assertNotEquals(inclusionProof.getRootHash(), "");

                // Verify the number of returned nodes in the inclusion proof path
                Assert.assertEquals(TreeUtils.calculateInclusionProofSize(inclusionProof.getTreeSize(),
                        inclusionProof.getIndex() + 1), inclusionProof.getPath().size());

                List<byte[]> inclusionProofPath = new ArrayList<>();
                for (String pathEntry : inclusionProof.getPath()) {
                    inclusionProofPath.add(Base64.getUrlDecoder().decode(pathEntry));
                }

                // Compute the Merkle leaf hash of the given audit log entry
                int index = inclusionProof.getIndex();
                byte[] hashToVerify = CryptoUtils.hash(ArrayUtils.addByteToArray(
                        Base64.getUrlDecoder().decode(entryForVerification),
                        (byte)0x00));

                // Verify the inclusion of the given audit log entry using the retrieved Merkle inclusion proof data and
                // MTH
                Assert.assertTrue(InclusionProofVerification.verify(inclusionProofPath, hashToVerify, index,
                        inclusionProof.getTreeSize(), Base64.getUrlDecoder().decode(inclusionProof.getRootHash())));
            }


            // Retrieve the consistency proof data between two historical Merkle Tree Heads (MTHs) or between a
            // historical MTH and the latest MTH
            ConsistencyProof consistencyProof = client.getVerificationActions().getConsistencyProof(
                    historicalMth, applicationId, StringUtil.base64StringAddPadding(treeHead.getRootHash()));

            Assert.assertNotNull(consistencyProof);
            Assert.assertEquals(historicalMth, consistencyProof.getFirstHash());
            Assert.assertEquals(StringUtil.base64StringAddPadding(treeHead.getRootHash()),
                    consistencyProof.getSecondHash());
            Assert.assertTrue(consistencyProof.getFirstTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getSecondTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getPath().size() > 0);

            // Verify the number of returned nodes in the Merkle consistency proof path
            Assert.assertEquals(TreeUtils.calculateConsistencyProofSize(consistencyProof.getFirstTreeSize(),
                    consistencyProof.getSecondTreeSize()), consistencyProof.getPath().size());

            List<byte[]> consistencyProofPath = new ArrayList<>();
            for (String pathEntry : consistencyProof.getPath()) {
                consistencyProofPath.add(Base64.getUrlDecoder().decode(pathEntry));
            }

            // Verify the consistency between two historical Merkle Tree Heads (MTHs) or between a historical MTH and
            // the latest MTH
            Assert.assertTrue(ConsistencyProofVerification.verify(consistencyProofPath,
                    Base64.getUrlDecoder().decode(consistencyProof.getFirstHash()),
                    consistencyProof.getFirstTreeSize(),
                    Base64.getUrlDecoder().decode(consistencyProof.getSecondHash()),
                    consistencyProof.getSecondTreeSize()));

        } catch (ApiException e) {
            System.err.println("Exception when calling AuditLogControllerApi#logAuthAction");
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}