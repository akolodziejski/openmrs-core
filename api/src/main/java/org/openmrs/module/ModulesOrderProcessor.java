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
	       */
	/*
	private void addBeforeStartDependency(Module firstModule, Module secondModule){
	moduleDependency.add(new ModuleDependency(secondModule, firstModule));
	}
	
	*//**
	   * Add dependency: firstModule required secondModule
	   * @param firstModule required secondModule
	   * @param secondModule is needed by firstModule
	   * @throws CyclomaticDependencyException 
	   */
	/*
	private void addRequiredDependency(Module firstModule, Module secondModule){
	moduleDependency.add(new ModuleDependency(firstModule, secondModule));
	}*/

	/**
	 * Order modules to start based on theirs dependencies.
	 * 
	 * @param modulesToStart - collection of modules to order
	 * @return ordered modules
	 * 
	 * @throws CyclomaticDependencyException
	 * 
	 * @should order modules based on theirs dependencies
	 * @should throw CyclomaticDependencyException if modules cannot be ordered
	 */
	public List<Module> getStartOrder(Collection<Module> modulesToStart) throws CyclomaticDependencyException {
		
		ModuleDependenciesNet moduleDependenciesNet = new ModuleDependenciesNet(modulesToStart);
		return getStartOrder(moduleDependenciesNet);
	}
	
	private List<Module> getStartOrder(ModuleDependenciesNet net) throws CyclomaticDependencyException {
		
		List<Module> orderedModules = Lists.newArrayList();
		
		while (net.getModules().size() > 0) {
			
			Set<Module> readyToOrderModules = net.findLeafModules();
			
			if (readyToOrderModules.size() < 1) {
				throw new CyclomaticDependencyException();
				//TODO areo - add informative message
			}
			orderedModules.addAll(readyToOrderModules);
			
			net.removeModules(readyToOrderModules);
		}
		
		return orderedModules;
	}
	
	/*	private void removeModuleDependenciesByModules(
				Set<ModuleDependency> noFixedModuleDependencies,
				Set<Module> removedModules) {
			
			Set<ModuleDependency> dependencies = Sets.newHashSet(noFixedModuleDependencies);
			
			for (ModuleDependency moduleDependency : dependencies) {
				if(removedModules.contains(moduleDependency.getDependsOnModule())){
					noFixedModuleDependencies.remove(moduleDependency);
				}
			}
		}*/

	private static class ModuleCollectionHelper {
		
		static Module getModuleByName(String name, Collection<Module> modules) {
			for (Module module : modules) {
				if (module.getName().equals(name)) {
					return module;
				}
				;
			}
			return null; //TODO areo - consider use of exception	
		}
	}
	
	private static class ModuleDependenciesNet {
		
		private Collection<Module> modules;
		
		public ModuleDependenciesNet(Collection<Module> modulesToStart) {
			this.modules = Sets.newHashSet(modulesToStart);
		}
		
		public Set<Module> findLeafModules() {
			
			Set<ModuleDependency> modulesDependencies = getModulesDependencies();
			
			Set<Module> modulesWithDependencies = Sets.newHashSet();
			//Set<Module> modulesWhichAreDependency = Sets.newHashSet();
			
			for (ModuleDependency moduleDependency : modulesDependencies) {
				
				//		modulesWhichAreDependency.add(moduleDependency.getDependsOnModule());		
				modulesWithDependencies.add(moduleDependency.getModule());
			}
			
			HashSet<Module> modulesTmp = Sets.newHashSet(modules);
			modulesTmp.removeAll(modulesWithDependencies);
			return modulesTmp;
		}
		
		public void removeModules(Collection<Module> modulesToRemove) {
			this.modules.removeAll(modulesToRemove);
		}
		
		public Set<ModuleDependency> getModulesDependencies() {
			
			Set<ModuleDependency> modulesDependencies = new HashSet<ModuleDependency>();
			
			for (Module module : modules) {
				
				Set<ModuleDependency> dependenciesForModule = extractDependenciesForModule(module, modules);
				
				modulesDependencies.addAll(dependenciesForModule);
			}
			return modulesDependencies;
		}
		
		private Set<ModuleDependency> extractDependenciesForModule(Module module, Collection<Module> modulesToStart) {
			
			Set<ModuleDependency> moduleDependencies = new HashSet<ModuleDependency>();
			
			//check required modules
			for (Map.Entry<String, String> requiredModuleInfo : module.getRequiredModulesMap().entrySet()) {
				String requiredModuleName = requiredModuleInfo.getKey();
				Module requiredModule = ModuleCollectionHelper.getModuleByName(requiredModuleName, modulesToStart);
				
				//TODO areo  - check versions
				if (requiredModule != null) {
					moduleDependencies.add(new ModuleDependency(module, requiredModule));
				}
			}
			
			//check start-before modules
			for (Map.Entry<String, String> startBeforeModuleInfo : module.getStartBeforeModulesMap().entrySet()) {
				String startBeforeModuleName = startBeforeModuleInfo.getKey();
				Module startBeforeThisModule = ModuleCollectionHelper.getModuleByName(startBeforeModuleName, modulesToStart);
				
				//TODO areo - check versions
				if (startBeforeThisModule != null) {
					moduleDependencies.add(new ModuleDependency(startBeforeThisModule, module));
				}
			}
			
			// TODO Auto-generated method stub
			return moduleDependencies;
		}
		
		public Collection<Module> getModules() {
			return modules;
		}
		
	}
	
	private static class ModuleDependency {
		
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
		public String toString() {
			return "" + module + " -> " + dependsOnModule;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dependsOnModule == null) ? 0 : dependsOnModule.hashCode());
			result = prime * result + ((module == null) ? 0 : module.hashCode());
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
