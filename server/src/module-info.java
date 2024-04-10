module server {
    requires java.rmi;
    opens iPackage to java.rmi;
    exports iPackage;
}