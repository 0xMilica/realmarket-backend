package io.realmarket.propeler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("UserKYCDocument")
@Entity(name = "UserKYCDocument")
public class UserKYCDocument extends Document {

    @JoinColumn(name = "userKycId", foreignKey = @ForeignKey(name = "document_fk_on_user_kyc"))
    @ManyToOne
    private UserKYC userKYC;

    @Builder(builderMethodName = "userKYCDocumentBuilder")
    public UserKYCDocument(
            DocumentAccessLevel accessLevel,
            DocumentType type,
            String url,
            Instant uploadDate,
            UserKYC userKYC) {
        super(null, accessLevel, type, url, uploadDate);
        this.userKYC = userKYC;
    }
}
