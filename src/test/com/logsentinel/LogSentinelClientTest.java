package com.logsentinel;
import com.logsentinel.client.model.*;
import com.logsentinel.merkletree.utils.TreeUtils;
import com.logsentinel.merkletree.verification.ConsistencyProofVerification;
import com.logsentinel.merkletree.verification.InclusionProofVerification;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class LogSentinelClientTest {
    private String applicationId = "ae37f8c0-7f38-11e8-bf35-cbf6b8eea46f";
    private String organizationId = "ae1c3360-7f38-11e8-bf35-cbf6b8eea46f";
    private String secret = "846b72776182fe44a9e31dc009f9d97989b64e251d323acff43dc3d665e6ac15";

    @Test
    public void getVerificationActions() {
        LogSentinelClientBuilder builder = LogSentinelClientBuilder
                .create(applicationId, organizationId, secret);
        builder.setBasePath("http://localhost:8080");
        LogSentinelClient client = builder.build();


        String hash1 = "0qHnEuGmu5I5vBIURvcjkTDw3LF_t_BQLUWRvoutPCSaCU1-j9lafQ8A_qbJFfkiUYU1fHtz9OyCwWP_XUjHbw==";
        String hash2 = "dht-bOoP-g1LPgwO7hNgvahGf2Pv87VxpMQ7jiufF2XratgOZJF5FmZ29lfwuJvJywtFE1fQyJ9HV6hVABo5Tg==";

        try {
            LogInfo logInfo = client.getVerificationActions().getMerkleTreeInfoUsingGET();

            Assert.assertNotNull(logInfo);
            Assert.assertEquals("2.16.840.1.101.3.4.2.3", logInfo.getLeafHashAlgorithmOid());
            Assert.assertEquals("2.16.840.1.101.3.4.2.1", logInfo.getHashAlgorithmOid());
            Assert.assertEquals("1.2.840.113549.1.1.13", logInfo.getTimestampAlgorithmOid());

            TreeHead treeHead = client.getVerificationActions().getLatestTreeHeadUsingGET(applicationId);

            Assert.assertNotNull(treeHead);
            Assert.assertTrue(treeHead.getTreeSize() > 0);
            Assert.assertNotEquals(treeHead.getTimestampToken(), "");
            Assert.assertNotEquals(treeHead.getRootHash(), "");

            List<String> entriesForVerification = new ArrayList<>();

            List<AuditLogEntry> logEntries = client.getVerificationActions().getEntriesBetweenHashesUsingGET(hash1,
                    hash2, applicationId);

            Assert.assertNotNull(logEntries);
            Assert.assertTrue(logEntries.size() > 0);

            for (AuditLogEntry entry : logEntries) {
                Assert.assertNotNull(entry.getHash());
                Assert.assertNotNull(entry.getTimestampTime());
                Assert.assertNotNull(entry.getId());

                String prevEntryHash = new String();

                if (entry.getPreviousEntryId() != null) {
                    AuditLogEntry prevEntry = client.getAuditLogActions().getEntryByIdUsingGET(applicationId,
                            entry.getPreviousEntryId());
                    prevEntryHash = prevEntry.getHash();
                } else {
                    prevEntryHash = "";
                }

                String expectedHash =  Base64.getUrlEncoder().encodeToString(DigestUtils.sha512(
                        entry.getHashableContent() + prevEntryHash));

                Assert.assertEquals(entry.getHash(), expectedHash);

                String standaloneHash = client.getHashActions().getHashUsingPOST(applicationId,
                        UUID.fromString(entry.getId()));

                Assert.assertNotNull(standaloneHash);

                entriesForVerification.add(standaloneHash);
            }

            /*
            for (String entryForVerification : entriesForVerification) {
                InclusionProof inclusionProof = client.getVerificationActions().getInclusionProofUsingGET(
                        entryForVerification,
                        applicationId);

                Assert.assertNotNull(inclusionProof);
                Assert.assertNotEquals(inclusionProof.getHash(), "");
                Assert.assertEquals(entryForVerification,
                        inclusionProof.getHash());
                Assert.assertTrue(inclusionProof.getIndex() >= 0);
                Assert.assertTrue(inclusionProof.getPath().size() > 0);
                Assert.assertTrue(inclusionProof.getTreeSize() > 0);
                Assert.assertNotEquals(inclusionProof.getRootHash(), "");

                Assert.assertTrue(inclusionProof.getPath().size() == TreeUtils.calculateInclusionProofSize(
                        inclusionProof.getTreeSize(), inclusionProof.getIndex() + 1
                ));

                List<byte[]> inclusionProofPath = new ArrayList<>();
                for (String pathEntry : inclusionProof.getPath()) {
                    inclusionProofPath.add(Base64.getUrlDecoder().decode(pathEntry));
                }

                int index = inclusionProof.getIndex();

                Assert.assertTrue(InclusionProofVerification.verify(inclusionProofPath,
                        Base64.getUrlDecoder().decode(entryForVerification), index,
                        inclusionProof.getTreeSize(), Base64.getUrlDecoder().decode(inclusionProof.getRootHash())));
            }

            ConsistencyProof consistencyProof = client.getVerificationActions().getConsistencyProofUsingGET(
                    hash1,
                    applicationId, "");

            Assert.assertNotNull(consistencyProof);
            Assert.assertNotEquals(consistencyProof.getFirstHash(), "");
            Assert.assertEquals(hash1,
                    consistencyProof.getFirstHash());
            Assert.assertNotEquals(consistencyProof.getSecondHash(), "");
            Assert.assertEquals("", consistencyProof.getSecondHash());
            Assert.assertTrue(consistencyProof.getFirstTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getSecondTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getPath().size() > 0);

            Assert.assertTrue(consistencyProof.getPath().size() == TreeUtils.calculateConsistencyProofSize(
                    consistencyProof.getFirstTreeSize(),
                    consistencyProof.getSecondTreeSize()));

            List<byte[]> consistenctProofPath = new ArrayList<>();
            for (String pathEntry : consistencyProof.getPath()) {
                consistenctProofPath.add(Base64.getUrlDecoder().decode(pathEntry));
            }

            Assert.assertTrue(ConsistencyProofVerification.verify(consistenctProofPath,
                    Base64.getDecoder().decode(consistencyProof.getFirstHash()), consistencyProof.getFirstTreeSize(),
                    Base64.getDecoder().decode(consistencyProof.getSecondHash()), consistencyProof.getSecondTreeSize()));

                    */
            /*
            try {
                byte[] publicKey = Base64.getUrlDecoder().decode(result.getPublicKey());

                ByteArrayInputStream bIn = new ByteArrayInputStream(publicKey);
                ASN1InputStream aIn = new ASN1InputStream(bIn);

                ASN1ObjectIdentifier c = new ASN1ObjectIdentifier("1.2.840.113549.1.1.1");
                AlgorithmIdentifier alg = new AlgorithmIdentifier(c);
                SubjectPublicKeyInfo subk = new SubjectPublicKeyInfo(alg, aIn.readObject());

                X509EncodedKeySpec spec =
                        new X509EncodedKeySpec(subk.parsePublicKey().getEncoded());
                KeyFactory kf = KeyFactory.getInstance("RSA");

                Assert.assertNotNull(kf.generatePublic(spec));
            }
            catch (NoSuchAlgorithmException|InvalidKeySpecException|IOException e) {
                System.err.println("Exception when importing public key");
                e.printStackTrace();
            }
            */
        } catch (ApiException e) {
            System.err.println("Exception when calling AuditLogControllerApi#logAuthAction");
            e.printStackTrace();
        }
    }
}