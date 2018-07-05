package com.logsentinel.util;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.DefaultCMSSignatureAlgorithmNameGenerator;
import org.bouncycastle.cms.bc.BcRSASignerInfoVerifierBuilder;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampToken;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TimeStampUtil {

    /**
     * Verify a time stamp token over a given Merkle Tree Head (MTH).
     *
     * @param mth The Base64-encoded Merkle Tree Head (MTH)
     * @param timeStampToken The Base64-encoded time stamp token returned by the server
     * @param signingCertificate The Base64-encoded DER representation of the certificate used for signing the time
     *                           stamp token
     * @return Verification result
     */
    public static boolean verifyMthTimeStamp(String mth, String timeStampToken, String signingCertificate) {
        try {
            // Verify the time stamp over the latest Merkle Tree Head (MTH)
            TimeStampToken tsToken = new TimeStampToken(
                    new CMSSignedData(Base64.getDecoder().decode(timeStampToken)));

            // Get the length of the digest declared in the time stamp
            int digestSize = MessageDigest.getInstance(
                    tsToken.getTimeStampInfo().getHashAlgorithm().getAlgorithm().getId()).getDigestLength();

            if (digestSize != tsToken.getTimeStampInfo().getMessageImprintDigest().length) {
                return false;
            }

            // Verify that the signed content in the time stamp corresponds to the retrieved Merkle Tree Head (MTH)
            if (!mth.equals(Base64.getUrlEncoder().encodeToString(tsToken.getTimeStampInfo().getMessageImprintDigest())
                    .replace("=", ""))) {
                return false;
            }

            X509CertificateHolder holder = new X509CertificateHolder(Base64.getDecoder().decode(signingCertificate));

            BcRSASignerInfoVerifierBuilder verifierBuilder = new BcRSASignerInfoVerifierBuilder(
                    new DefaultCMSSignatureAlgorithmNameGenerator(),
                    new DefaultSignatureAlgorithmIdentifierFinder(),
                    new DefaultDigestAlgorithmIdentifierFinder(), new BcDigestCalculatorProvider());

            // Verify the signature
            return tsToken.isSignatureValid(verifierBuilder.build(holder));
        }
        catch (IOException |CMSException |TSPException |OperatorCreationException |NoSuchAlgorithmException
                | IllegalArgumentException e) {
            System.err.println("Exception when verifying MTH time stamp token");
            e.printStackTrace();

            return false;
        }
    }
}
