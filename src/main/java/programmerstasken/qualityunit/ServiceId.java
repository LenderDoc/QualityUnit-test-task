package programmerstasken.qualityunit;

/**
 * The special value "*" stored as a ServiceId where service=0
 */
class ServiceId {

    private int service;
    private int variation;

    ServiceId(String serviceId) {
        if (serviceId.equals("*")) {
            return;
        }

        String values[] = serviceId.split("\\.");

        if (values.length == 0 || values.length > 2) {
            throw new IllegalArgumentException(serviceId);
        }

        service = Integer.valueOf(values[0]);

        if (values.length == 2) {
            variation = Integer.valueOf(values[1]);
        }
    }

    boolean match(ServiceId matchTo) {
        if (matchTo.service == 0) {      //  The special value "*" 
            return true;
        }

        return service == matchTo.service
                && (variation == matchTo.variation || matchTo.variation == 0);
    }

    @Override
    public String toString() {
        if (service == 0) {
            return "*";
        }

        if (variation == 0) {
            return Integer.toString(service);
        }
        
        return String.format("%d.%d", service, variation);
    }
}
