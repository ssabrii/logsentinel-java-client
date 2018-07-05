package com.logsentinel;
import com.logsentinel.client.model.*;
import com.logsentinel.merkletree.utils.TreeUtils;
import com.logsentinel.merkletree.verification.ConsistencyProofVerification;
import com.logsentinel.merkletree.verification.InclusionProofVerification;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.bc.BcRSASignerInfoVerifierBuilder;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.util.CollectionStore;
import org.junit.Assert;
import org.junit.Test;

import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.cms.CMSSignedData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class LogSentinelClientTest {
    private String applicationId = "ae37f8c0-7f38-11e8-bf35-cbf6b8eea46f";
    private String organizationId = "ae1c3360-7f38-11e8-bf35-cbf6b8eea46f";
    private String secret = "846b72776182fe44a9e31dc009f9d97989b64e251d323acff43dc3d665e6ac15";

    private Optional<X509CertificateHolder> getCertificateHolder(CMSSignedData signedData) throws IOException {
        CollectionStore<X509CertificateHolder> store = (CollectionStore<X509CertificateHolder>) signedData
                .getCertificates();
        Iterator<X509CertificateHolder> iterator = store.iterator();
        if (iterator.hasNext()) {
            return Optional.of(store.iterator().next());
        } else {
            return Optional.empty();
        }
    }

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
        String hash2 = "dht-bOoP-g1LPgwO7hNgvahGf2Pv87VxpMQ7jiufF2XratgOZJF5FmZ29lfwuJvJywtFE1fQyJ9HV6hVABo5Tg==";

        String tst = "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";

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
            */

            ConsistencyProof consistencyProof = client.getVerificationActions().getConsistencyProofUsingGET(
                    historicalMth, applicationId, base64StringAddPadding(treeHead.getRootHash()));

            Assert.assertNotNull(consistencyProof);
            Assert.assertEquals(historicalMth, consistencyProof.getFirstHash());
            Assert.assertEquals(base64StringAddPadding(treeHead.getRootHash()), consistencyProof.getSecondHash());
            Assert.assertTrue(consistencyProof.getFirstTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getSecondTreeSize() > 0);
            Assert.assertTrue(consistencyProof.getPath().size() > 0);

            Assert.assertTrue(consistencyProof.getPath().size() == TreeUtils.calculateConsistencyProofSize(
                    consistencyProof.getFirstTreeSize(),
                    consistencyProof.getSecondTreeSize()));

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