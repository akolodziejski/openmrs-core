package org.openmrs.module;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ModulesOrderProcessor {
	
	
/*	*//**
	 * Add dependency : firstModule need to be started before secondModule
	 * @param firstModule need to be started before secondModule
	 * @param secondModule need to be started after firstModule
	 *//*
	private void addBeforeStartDependency(Module firstModule, Module secondModule){
		moduleDependency.add(new ModuleDependency(secondModule, firstModule));
	}
	
	*//**
	 * Add dependency: firstModule required secondModule
	 * @param firstModule required secondModule
	 * @param secondModule is needed by firstModule
	 *//*
	private void addRequiredDependency(Module firstModule, Module secondModule){
		moduleDependency.add(new ModuleDependency(firstModule, secondModule));
	}*/
	
	
	public List<Module> getStartOrder( Collection<Module> modulesToStart){
		Set<ModuleDependency> moduleDependencies = extractModulesDependencies(modulesToStart);
		return getStartOrder(moduleDependencies);
	}
	
	
	private List<Module> getStartOrder(final Set<ModuleDependency> modulesDependencies) {
	
		Set<ModuleDependency> noFixedModuleDependencies	= Sets.newHashSet(modulesDependencies);
		List<Module> orderedModules = Lists.newArrayList();
		
		while(noFixedModuleDependencies.size() > 0){
			
			List<Module> readyToOrderModules = findReadyToOrderModules(noFixedModuleDependencies);
			
			if (readyToOrderModules.size() < 1){
				//TODO areo - must be cyclic dependency here
			}
			orderedModules.addAll(readyToOrderModules);
			noFixedModuleDependencies.removeAll(orderedModules);
		}
		
		
		return null;
	}


	private List<Module> findReadyToOrderModules(
			Set<ModuleDependency> noFixedModuleDependencies) {
		// TODO Auto-generated method stub
		return null;
	}


	private Set<ModuleDependency> extractModulesDependencies( Collection<Module> modulesToStart) {
		
		Set<ModuleDependency> modulesDependencies = new HashSet<ModuleDependency>();
		
		
		for (Module module : modulesToStart) {
			
			Set<ModuleDependency> dependenciesForModule 
				= extractDependenciesForModule(module, modulesToStart);
			
			modulesDependencies.addAll(dependenciesForModule);
		}
		return modulesDependencies;
	}


	private Set<ModuleDependency> extractDependenciesForModule(Module module, Collection<Module> modulesToStart) {
		
		Set<ModuleDependency> moduleDependencies = new HashSet<ModuleDependency>();
		
		//check required modules
		for (Map.Entry<String, String> requiredModuleInfo : module.getRequiredModulesMap().entrySet()) {
			String requiredModuleName = requiredModuleInfo.getKey();
			Module requiredModule 
				= ModuleCollectionHelper.getModuleByName(requiredModuleName, modulesToStart);

			//TODO areo  - check versions
			moduleDependencies.add(new ModuleDependency(module, requiredModule));
			
		}
		
		//check start-before modules
		for (Map.Entry<String, String> startBeforeModuleInfo : module.getStartBeforeModulesMap().entrySet()) {
			String startBeforeModuleName = startBeforeModuleInfo.getKey();
			Module startBeforeThisModule 
				= ModuleCollectionHelper.getModuleByName(startBeforeModuleName, modulesToStart);
			
			//TODO areo - check versions
			moduleDependencies.add(new ModuleDependency(startBeforeThisModule, module));
		}
		
		// TODO Auto-generated method stub
		return moduleDependencies;
	}


	private static class ModuleCollectionHelper{
		static Module getModuleByName(String name, Collection<Module> modules){
			for (Module module : modules) {
				if(module.getName().equals(name)){
					return module;
				};
			}
			return null; //TODO areo - consider use of exception	
		}
	}
	
	private static class ModuleDependency{
		
		private Module module;
		private Module dependsOnModule;
		
		public ModuleDependency(Module module, Module dependsOnModule) {
			this.module = module;
			this.dependsOnModule = dependsOnModule;
		}

		public Module getModule() {
			return module;
		}

		public Module getDependsOnModule() {
			return dependsOnModule;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((dependsOnModule == null) ? 0 : dependsOnModule
							.hashCode());
			result = prime * result
					+ ((module == null) ? 0 : module.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ModuleDependency other = (ModuleDependency) obj;
			if (dependsOnModule == null) {
				if (other.dependsOnModule != null)
					return false;
			} else if (!dependsOnModule.equals(other.dependsOnModule))
				return false;
			if (module == null) {
				if (other.module != null)
					return false;
			} else if (!module.equals(other.module))
				return false;
			return true;
		}
		
	}
}
