//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.dubbo.rpc.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import org.springframework.MyLog;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.config.ModuleEnvironment;
import org.apache.dubbo.common.context.ModuleExt;
import org.apache.dubbo.common.deploy.ApplicationDeployer;
import org.apache.dubbo.common.deploy.DeployState;
import org.apache.dubbo.common.deploy.ModuleDeployer;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.ExtensionScope;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.Assert;
import org.apache.dubbo.common.utils.ClassUtils;
import org.apache.dubbo.config.context.ModuleConfigManager;

public class ModuleModel extends ScopeModel {
    private static final Logger logger = LoggerFactory.getLogger(ModuleModel.class);
    public static final String NAME = "ModuleModel";
    private final ApplicationModel applicationModel;
    private volatile ModuleServiceRepository serviceRepository;
    private volatile ModuleEnvironment moduleEnvironment;
    private volatile ModuleConfigManager moduleConfigManager;
    private volatile ModuleDeployer deployer;
    private boolean lifeCycleManagedExternally;

    protected ModuleModel(ApplicationModel applicationModel) {
        this(applicationModel, false);
    }

    protected ModuleModel(ApplicationModel applicationModel, boolean isInternal) {
        super(applicationModel, ExtensionScope.MODULE, isInternal);
        this.lifeCycleManagedExternally = false;
        synchronized (this.instLock) {
            Assert.notNull(applicationModel, "ApplicationModel can not be null");
            this.applicationModel = applicationModel;
            applicationModel.addModule(this, isInternal);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(this.getDesc() + " is created");
            }

            this.initialize();
            this.serviceRepository = new ModuleServiceRepository(this);
            this.initModuleExt();
            ExtensionLoader<ScopeModelInitializer> initializerExtensionLoader = this.getExtensionLoader(ScopeModelInitializer.class);
            Set<ScopeModelInitializer> initializers = initializerExtensionLoader.getSupportedExtensionInstances();
            Iterator var6 = initializers.iterator();

            while (var6.hasNext()) {
                ScopeModelInitializer initializer = (ScopeModelInitializer) var6.next();
                initializer.initializeModuleModel(this);
            }

            Assert.notNull(this.getServiceRepository(), "ModuleServiceRepository can not be null");
            Assert.notNull(this.getConfigManager(), "ModuleConfigManager can not be null");
            Assert.assertTrue(this.getConfigManager().isInitialized(), "ModuleConfigManager can not be initialized");
            ApplicationDeployer applicationDeployer = applicationModel.getDeployer();
            if (applicationDeployer != null) {
                applicationDeployer.notifyModuleChanged(this, DeployState.PENDING);
            }

        }

        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
            MyLog.getInstance().write((Thread.currentThread().getName()+ "=============================\n").toString().getBytes());
            for (StackTraceElement element : stackTraces) {
                StringBuffer sb = new StringBuffer();
                sb.append(element.getClassName()).append(".").append(element.getMethodName()).append(":").append(element.getLineNumber());
                MyLog.getInstance().write((sb.toString() + "\n").toString().getBytes());
            }
    }

    private void initModuleExt() {
        Set<ModuleExt> exts = this.getExtensionLoader(ModuleExt.class).getSupportedExtensionInstances();
        Iterator var2 = exts.iterator();

        while (var2.hasNext()) {
            ModuleExt ext = (ModuleExt) var2.next();
            ext.initialize();
        }

    }

    protected void onDestroy() {
        synchronized (this.instLock) {
            this.applicationModel.removeModule(this);
            if (this.deployer != null) {
                this.deployer.preDestroy();
            }

            if (this.deployer != null) {
                this.deployer.postDestroy();
            }

            this.notifyDestroy();
            if (this.serviceRepository != null) {
                this.serviceRepository.destroy();
                this.serviceRepository = null;
            }

            if (this.moduleEnvironment != null) {
                this.moduleEnvironment.destroy();
                this.moduleEnvironment = null;
            }

            if (this.moduleConfigManager != null) {
                this.moduleConfigManager.destroy();
                this.moduleConfigManager = null;
            }

            this.applicationModel.tryDestroy();
        }
    }

    public ApplicationModel getApplicationModel() {
        return this.applicationModel;
    }

    public ModuleServiceRepository getServiceRepository() {
        return this.serviceRepository;
    }

    public void addClassLoader(ClassLoader classLoader) {
        super.addClassLoader(classLoader);
        if (this.moduleEnvironment != null) {
            this.moduleEnvironment.refreshClassLoaders();
        }

    }

    public ModuleEnvironment modelEnvironment() {
        if (this.moduleEnvironment == null) {
            this.moduleEnvironment = (ModuleEnvironment) this.getExtensionLoader(ModuleExt.class).getExtension("moduleEnvironment");
        }

        return this.moduleEnvironment;
    }

    public ModuleConfigManager getConfigManager() {
        if (this.moduleConfigManager == null) {
            this.moduleConfigManager = (ModuleConfigManager) this.getExtensionLoader(ModuleExt.class).getExtension("moduleConfig");
        }

        return this.moduleConfigManager;
    }

    public ModuleDeployer getDeployer() {
        return this.deployer;
    }

    public void setDeployer(ModuleDeployer deployer) {
        this.deployer = deployer;
    }

    protected Lock acquireDestroyLock() {
        return this.getApplicationModel().getFrameworkModel().acquireDestroyLock();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setModuleEnvironment(ModuleEnvironment moduleEnvironment) {
        this.moduleEnvironment = moduleEnvironment;
    }

    public ConsumerModel registerInternalConsumer(Class<?> internalService, URL url, ServiceDescriptor serviceDescriptor, Object proxyObject) {
        ServiceMetadata serviceMetadata = new ServiceMetadata();
        serviceMetadata.setVersion(url.getVersion());
        serviceMetadata.setGroup(url.getGroup());
        serviceMetadata.setDefaultGroup(url.getGroup());
        serviceMetadata.setServiceInterfaceName(internalService.getName());
        serviceMetadata.setServiceType(internalService);
        String serviceKey = URL.buildKey(internalService.getName(), url.getGroup(), url.getVersion());
        serviceMetadata.setServiceKey(serviceKey);
        ConsumerModel consumerModel = new ConsumerModel(serviceMetadata.getServiceKey(), proxyObject, serviceDescriptor == null ? this.serviceRepository.lookupService(serviceMetadata.getServiceInterfaceName()) : serviceDescriptor, this, serviceMetadata, new HashMap(0), ClassUtils.getClassLoader(internalService));
        logger.info("Dynamically registering consumer model " + serviceKey + " into model " + this.getDesc());
        this.serviceRepository.registerConsumer(consumerModel);
        return consumerModel;
    }

    public ConsumerModel registerInternalConsumer(Class<?> internalService, URL url, ServiceDescriptor serviceDescriptor) {
        return this.registerInternalConsumer(internalService, url, serviceDescriptor, (Object) null);
    }

    public ConsumerModel registerInternalConsumer(Class<?> internalService, URL url) {
        return this.registerInternalConsumer(internalService, url, (ServiceDescriptor) null, (Object) null);
    }

    public boolean isLifeCycleManagedExternally() {
        return this.lifeCycleManagedExternally;
    }

    public void setLifeCycleManagedExternally(boolean lifeCycleManagedExternally) {
        this.lifeCycleManagedExternally = lifeCycleManagedExternally;
    }
}
