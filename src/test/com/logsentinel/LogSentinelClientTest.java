package com.logsentinel;
import com.logsentinel.client.model.*;
import com.logsentinel.merkletree.utils.ArrayUtils;
import com.logsentinel.merkletree.utils.CryptoUtils;
import com.logsentinel.merkletree.utils.TreeUtils;
import com.logsentinel.merkletree.verification.ConsistencyProofVerification;
import com.logsentinel.merkletree.verification.InclusionProofVerification;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.bc.BcRSASignerInfoVerifierBuilder;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.tsp.TSPException;
import org.junit.Assert;
import org.junit.Test;

import org.bouncycastle.tsp.TimeStampToken;

import java.io.IOException;
import java.security.*;
import java.util.*;

@SuppressWarnings("SpellCheckingInspection")
public class LogSentinelClientTest {
    final String applicationId = "ae37f8c0-7f38-11e8-bf35-cbf6b8eea46f";
    final String organizationId = "ae1c3360-7f38-11e8-bf35-cbf6b8eea46f";
    final String secret = "846b72776182fe44a9e31dc009f9d97989b64e251d323acff43dc3d665e6ac15";

    private String base64StringAddPadding(String base64) {
        int rem = Math.floorMod(base64.length(), 4);

        if (rem == 2) {
            base64 = base64 + "==";
        }
        else if (rem == 3) {
            base64 = base64 + "=";
        }

        return base64;
    }

    @Test
    public void getVerificationActions() {
        LogSentinelClientBuilder builder = LogSentinelClientBuilder
                .create(applicationId, organizationId, secret);
        builder.setBasePath("http://localhost:8080");
        LogSentinelClient client = builder.build();


        String hash1 = "0qHnEuGmu5I5vBIURvcjkTDw3LF_t_BQLUWRvoutPCSaCU1-j9lafQ8A_qbJFfkiUYU1fHtz9OyCwWP_XUjHbw==";
        String hash2 = "aGpNDMW5vIJ1Mbe9fb-SSCyQoH6ZFigFCJ2ZvGIUn2pIWF00IaOzRNTpfckvwF7cmyXLJFnM3-9VOUNVKyLc9g==";

        String logSentinelTsCert = "MIICzjCCAbagAwIBAgIEWPIrGjANBgkqhkiG9w0BAQ0FADAbMRkwFwYDVQQDDBBhdWRpdGxvZy10c2Eta2V5MB4XDTE3MDQxNTE0MTcyM1oXDTIyMDQxNTE0MTcyM1owGzEZMBcGA1UEAwwQYXVkaXRsb2ctdHNhLWtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJ25X+ady+Af1K1AcNGJAVIJY2qR47DXF9gHFSXkV4fEuHvnEPzCozXNNpgwHbVVEJs0mUsaEG+MukZtJ1WWJha/qFYB9eMotYucVwUt1YulUbAJbWLb99oMJ8KyHWJtVFTfHJL+j3DScLzBQ0QglG7RiOc7gQohBnamtwe7ayIIOa/BJREYtK5o9rBLSddGeXZjoJzSrXARvaPnDolHuqK/eseFeLWJN0IvykCK/On3FCeWVtrzPlIrm1NVaVcyrK1x3j5P5I9pYZ2b32446FyXsQZlr0TvKvTbs5eKNGJ4270YVqYmSM6aZZH+x5X3oNujRk9q6OKepPv4vr9dRDcCAwEAAaMaMBgwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwgwDQYJKoZIhvcNAQENBQADggEBAJVn4p9ftwXqOsGyLuZYkCA2PxsSrMQXbrIk4geWsD7U9ZV/2yEuOKWKRmBna8i+d/g+UmNKo2EVYUaVDb+FxXbJswGSh/WHsCmVKn62iR4JldbNZHgQEBXRndxjfzigBqzbtPTIx7ivk7RyX2eSv4sA4E/Wb3Pshiqt5n9TZqDSeCUsshKOpE6WBdDyApsjDiJ/W/xhy9x9RhpAJdni+W0NgGQK/a96fxoKB75zoKqD1gnmr0WojQcDFFwMxLnVD0j0m9UECWJxI/67RkQEvequD13nZShseH4cF8hGLYF/rK0eDbjleBpAG4GBEpVY2jdVKU3KaHMdOFD+kWCT2+0=";

        String historicalMth = "PBPLkDuEYgo-C55y59egFWFm9FUpDu4xUf60uw4rIpI=";

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

            try {
                TimeStampToken tsToken = new TimeStampToken(
                        new CMSSignedData(
                                Base64.getDecoder().decode(treeHead.getTimestampToken())));

                int digestSize = MessageDigest.getInstance(
                        tsToken.getTimeStampInfo().getHashAlgorithm().getAlgorithm().getId()).getDigestLength();

                Assert.assertEquals(digestSize, tsToken.getTimeStampInfo().getMessageImprintDigest().length);

                Assert.assertEquals(treeHead.getRootHash(),
                        Base64.getUrlEncoder().encodeToString(tsToken.getTimeStampInfo().getMessageImprintDigest())
                            .replace("=", ""));

                X509CertificateHolder holder = new X509CertificateHolder(Base64.getDecoder().decode(logSentinelTsCert));

                BcRSASignerInfoVerifierBuilder verifierBuilder = new BcRSASignerInfoVerifierBuilder(
                        new DefaultCMSSignatureAlgorithmNameGenerator(),
                        new DefaultSignatureAlgorithmIdentifierFinder(),
                        new DefaultDigestAlgorithmIdentifierFinder(), new BcDigestCalculatorProvider());

                Assert.assertTrue(tsToken.isSignatureValid(verifierBuilder.build(holder)));
            }
            catch (IOException|CMSException|TSPException|OperatorCreationException|NoSuchAlgorithmException e) {
                System.err.println("Exception when verifying time stamp token");
                e.printStackTrace();
            }

            List<String> entriesForVerification = new ArrayList<>();

            List<AuditLogEntry> logEntries = client.getVerificationActions().getEntriesBetweenHashesUsingGET(hash1,
                    hash2, applicationId);

            Assert.assertNotNull(logEntries);
            Assert.assertTrue(logEntries.size() > 0);

            boolean logChainingVerificationError = false;

            for (int i = 0; i <= logEntries.size() - 1; i++) {
                Assert.assertNotNull(logEntries.get(i).getHash());
                Assert.assertNotNull(logEntries.get(i).getTimestampTime());
                Assert.assertNotNull(logEntries.get(i).getId());

                String standaloneHash = client.getHashActions().getHashUsingPOST(applicationId,
                        UUID.fromString(logEntries.get(i).getId()));

                Assert.assertNotNull(standaloneHash);

                entriesForVerification.add(standaloneHash);

                String prevEntryHash = i > 0 ? logEntries.get(i - 1).getHash() : "";

                String expectedEntryHash =  Base64.getUrlEncoder().encodeToString(DigestUtils.sha512(
                        logEntries.get(i).getHashableContent() + prevEntryHash));

                if (!expectedEntryHash.equals(logEntries.get(i).getHash())) {
                    logChainingVerificationError = true;
                }
            }

            Assert.assertFalse(logChainingVerificationError);


            for (String entryForVerification : entriesForVerification) {
                InclusionProof inclusionProof = client.getVerificationActions().getInclusionProofUsingGET(
                        entryForVerification,
                        applicationId);

                Assert.assertNotNull(inclusionProof);
                Assert.assertEquals(entryForVerification,
                        inclusionProof.getHash());
                Assert.assertTrue(inclusionProof.getIndex() >= 0);
                Assert.assertTrue(inclusionProof.getPath().size() > 0);
                Assert.assertTrue(inclusionProof.getTreeSize() > 0);
                Assert.assertNotEquals(inclusionProof.getRootHash(), "");

                Assert.assertEquals(TreeUtils.calculateInclusionProofSize(inclusionProof.getTreeSize(),
                        inclusionProof.getIndex() + 1), inclusionProof.getPath().size());

                List<byte[]> inclusionProofPath = new ArrayList<>();
                for (String pathEntry : inclusionProof.getPath()) {
                    inclusionProofPath.add(Base64.getUrlDecoder().decode(pathEntry));
                }

                int index = inclusionProof.getIndex();
                byte[] hashToVerify = CryptoUtils.hash(ArrayUtils.addByteToArray(
                        Base64.getUrlDecoder().decode(entryForVerification),
                        (byte)0x00));

                Assert.assertTrue(InclusionProofVerification.verify(inclusionProofPath, hashToVerify, index,
                        inclusionProof.getTreeSize(), Base64.getUrlDecoder().decode(inclusionProof.getRootHash())));
            }


            ConsistencyProof consistencyProof = client.getVerificationActions().getConsistencyProofUsingGET(
                    historicalMth, applicationId, base64StringAddPadding(treeHead.getRootHash()));

            Assert.assertNotNull(consistencyProof);
            Assert.assertEquals(historicalMth, consistencyProof.getFirstHash());
            Assert.assertEquals(base64StringAddPadding(treeHead.getRootHash()), consistencyProof.getSecondHash());
            Assert.assertTrue(consistencyProof.getFirstTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getSecondTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getPath().size() > 0);

            Assert.assertEquals(TreeUtils.calculateConsistencyProofSize(consistencyProof.getFirstTreeSize(),
                    consistencyProof.getSecondTreeSize()), consistencyProof.getPath().size());

            List<byte[]> consistencyProofPath = new ArrayList<>();
            for (String pathEntry : consistencyProof.getPath()) {
                consistencyProofPath.add(Base64.getUrlDecoder().decode(pathEntry));
            }

            Assert.assertTrue(ConsistencyProofVerification.verify(consistencyProofPath,
                    Base64.getUrlDecoder().decode(consistencyProof.getFirstHash()),
                    consistencyProof.getFirstTreeSize(),
                    Base64.getUrlDecoder().decode(consistencyProof.getSecondHash()),
                    consistencyProof.getSecondTreeSize()));

        } catch (ApiException e) {
            System.err.println("Exception when calling AuditLogControllerApi#logAuthAction");
            e.printStackTrace();
        }
    }
}