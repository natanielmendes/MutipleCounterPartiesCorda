package com.genpact.agreementnegotiation.state;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AgreementStateTemplate implements LinearState {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //Or whatever format fits best your needs.

    private UniqueIdentifier linearId;

    private String agrementName = null;
    private Date agrementInitiationDate = null;
    private Date agrementAgreedDate = null;
    private Party cptyInitiator = null;
    private Party cptyReciever = null;
    private Party lastUpdatedBy = null;
    private Date agrementLastAmendDate = null;
    private AgreementEnumState status;
    private List<SecureHash> attachmentHash;

    public AgreementStateTemplate() {
    }

    public AgreementStateTemplate(String agrementName, Date agrementInitiationDate,
                                  Date agrementAgreedDate, Party cptyInitiator, Party cptyReciever, Party lastUpdatedBy,
                                  Date agrementLastAmendDate, AgreementEnumState status) {

        this.agrementName = agrementName;
        this.agrementInitiationDate = agrementInitiationDate;
        this.agrementAgreedDate = agrementAgreedDate;
        this.cptyInitiator = cptyInitiator;
        this.cptyReciever = cptyReciever;
        this.lastUpdatedBy = lastUpdatedBy;
        this.agrementLastAmendDate = agrementLastAmendDate;
        this.status = status;
    }

    public void setLinearId(UniqueIdentifier linearId) {
        this.linearId = linearId;
    }

    public String getAgrementName() {
        return agrementName;
    }

    public void setAgrementName(String agrementName) {
        this.agrementName = agrementName;
    }

    public String getAgrementInitiationDate() {
        if (agrementInitiationDate != null) {
            String dateStr = FORMAT.format(agrementInitiationDate);
            return dateStr;
        }
        return "";
    }

    public void setAgrementInitiationDate(Date agrementInitiationDate) {
        this.agrementInitiationDate = agrementInitiationDate;
    }

    public String getAgrementAgreedDate() {
        if (agrementAgreedDate != null) {
            String dateStr = FORMAT.format(agrementAgreedDate);
            return dateStr;
        }
        return "";
    }

    public void setAgrementAgreedDate(Date agrementAgreedDate) {
        this.agrementAgreedDate = agrementAgreedDate;
    }

    public Party getCptyInitiator() {
        return cptyInitiator;
    }

    public void setCptyInitiator(Party cptyInitiator) {
        this.cptyInitiator = cptyInitiator;
    }

    public Party getCptyReciever() {
        return cptyReciever;
    }

    public void setCptyReciever(Party cptyReciever) {
        this.cptyReciever = cptyReciever;
    }

    public Party getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Party lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getAgrementLastAmendDate() {
        if (agrementLastAmendDate != null) {
            String dateStr = FORMAT.format(agrementAgreedDate);
            return dateStr;
        }
        return "";

    }

    public void setAgrementLastAmendDate(Date agrementLastAmendDate) {
        this.agrementLastAmendDate = agrementLastAmendDate;
    }

    public AgreementEnumState getStatus() {
        return status;
    }

    public void setStatus(AgreementEnumState status) {
        this.status = status;
    }

    public List<SecureHash> getAttachmentHash() {
        return attachmentHash;
    }

    public void setAttachmentHash(List<SecureHash> attachmentHash) {
        this.attachmentHash = attachmentHash;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return linearId;
    }


    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(cptyInitiator, cptyReciever);
    }

}