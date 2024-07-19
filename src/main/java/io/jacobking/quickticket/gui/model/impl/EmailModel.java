package io.jacobking.quickticket.gui.model.impl;

import io.jacobking.quickticket.gui.model.ViewModel;
import io.jacobking.quickticket.tables.pojos.EmailInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmailModel extends ViewModel<EmailInterface> {

    private static final String DEFAULT_PORT = "25";

    private final StringProperty  hostProperty           = new SimpleStringProperty();
    private final StringProperty  portProperty           = new SimpleStringProperty();
    private final BooleanProperty startTLSProperty       = new SimpleBooleanProperty();
    private final BooleanProperty overSSLProperty        = new SimpleBooleanProperty();
    private final BooleanProperty authenticationProperty = new SimpleBooleanProperty();
    private final StringProperty  usernameProperty       = new SimpleStringProperty();
    private final StringProperty  passwordProperty       = new SimpleStringProperty();
    private final StringProperty  fromAddressProperty    = new SimpleStringProperty();
    private final StringProperty  bccAddressProperty     = new SimpleStringProperty();
    private final BooleanProperty debuggingProperty      = new SimpleBooleanProperty();

    public EmailModel(final int id, String host, String port, boolean startTLS, boolean overSSL, boolean authentication, String username, String password,
                      String fromAddress, String bccAddress, boolean debugging) {
        super(id);
        this.hostProperty.setValue(host);
        this.portProperty.setValue(port);
        this.startTLSProperty.setValue(startTLS);
        this.overSSLProperty.setValue(overSSL);
        this.authenticationProperty.setValue(authentication);
        this.usernameProperty.setValue(username);
        this.passwordProperty.setValue(password);
        this.fromAddressProperty.setValue(fromAddress);
        this.bccAddressProperty.setValue(bccAddress);
        this.debuggingProperty.setValue(debugging);

        startTLSProperty.addListener(((observableValue, aBoolean, t1) -> {
            if (t1) {
                overSSLProperty.setValue(false);
            }
        }));

        overSSLProperty.addListener(((observableValue, aBoolean, t1) -> {
            if (t1) {
                startTLSProperty.setValue(false);
            }
        }));
    }

    public EmailModel(final EmailInterface email) {
        this(
                email.getId(),
                email.getHost(),
                email.getPort(),
                email.getStarttls(),
                email.getOverSslOrTls(),
                email.getAuthentication(),
                email.getUsername(),
                email.getPassword(),
                email.getFromAddress(),
                email.getBccAddress(),
                email.getDebugging()
        );
    }

    public String getHostProperty() {
        return hostProperty.get();
    }

    public void setHostProperty(String hostProperty) {
        this.hostProperty.set(hostProperty);
    }

    public StringProperty hostProperty() {
        return hostProperty;
    }

    public String getPortProperty() {
        return portProperty.get();
    }

    public void setPortProperty(String portProperty) {
        this.portProperty.set(portProperty);
    }

    public StringProperty portProperty() {
        return portProperty;
    }

    public boolean isStartTLSProperty() {
        return startTLSProperty.get();
    }

    public void setStartTLSProperty(boolean startTLSProperty) {
        this.startTLSProperty.set(startTLSProperty);
    }

    public BooleanProperty startTLSProperty() {
        return startTLSProperty;
    }

    public boolean isOverSSLProperty() {
        return overSSLProperty.get();
    }

    public void setOverSSLProperty(boolean overSSLProperty) {
        this.overSSLProperty.set(overSSLProperty);
    }

    public BooleanProperty overSSLProperty() {
        return overSSLProperty;
    }

    public boolean isAuthentication() {
        return authenticationProperty.get();
    }

    public BooleanProperty authenticationProperty() {
        return authenticationProperty;
    }

    public void setAuthenticationProperty(boolean authenticationProperty) {
        this.authenticationProperty.set(authenticationProperty);
    }

    public String getUsernameProperty() {
        return usernameProperty.get();
    }

    public void setUsernameProperty(String usernameProperty) {
        this.usernameProperty.set(usernameProperty);
    }

    public StringProperty usernameProperty() {
        return usernameProperty;
    }

    public String getPasswordProperty() {
        return passwordProperty.get();
    }

    public void setPasswordProperty(String passwordProperty) {
        this.passwordProperty.set(passwordProperty);
    }

    public StringProperty passwordProperty() {
        return passwordProperty;
    }

    public String getFromAddressProperty() {
        return fromAddressProperty.get();
    }

    public StringProperty fromAddressProperty() {
        return fromAddressProperty;
    }


    public String getBccAddress() {
        return bccAddressProperty.get();
    }

    public StringProperty bccAddressProperty() {
        return bccAddressProperty;
    }

    public void setBccAddressProperty(String bccAddressProperty) {
        this.bccAddressProperty.set(bccAddressProperty);
    }

    public boolean isDebugging() {
        return debuggingProperty.get();
    }

    public void setDebugging(boolean state) {
        this.debuggingProperty.setValue(state);
    }

    @Override
    public EmailInterface toEntity() {
        return new EmailInterface()
                .setId(getId())
                .setHost(getHostProperty())
                .setPort(getPortProperty().isEmpty() ? DEFAULT_PORT : getPortProperty())
                .setStarttls(isStartTLSProperty())
                .setOverSslOrTls(isOverSSLProperty())
                .setAuthentication(isAuthentication())
                .setUsername(getUsernameProperty())
                .setPassword(getPasswordProperty())
                .setFromAddress(getFromAddressProperty())
                .setBccAddress(getBccAddress())
                .setDebugging(isDebugging());
    }
}
