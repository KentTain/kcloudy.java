package kc.framework.tenant;

public class TenantContext {
	private static ThreadLocal<Tenant> currentTenant = new ThreadLocal<>();
	
	public static void setCurrentTenant(Tenant tenant) {
		currentTenant.set(tenant);
	}

	public static Tenant getCurrentTenant() {
		return currentTenant.get();
	}
	public static void clear() {
		currentTenant.set(null);
	}
}