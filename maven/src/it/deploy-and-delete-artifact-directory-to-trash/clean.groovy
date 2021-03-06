import org.carlspring.strongbox.client.ArtifactClient
import org.carlspring.strongbox.client.RestClient
import org.carlspring.strongbox.forms.configuration.RepositoryForm
import org.carlspring.strongbox.providers.datastore.StorageProviderEnum
import org.carlspring.strongbox.providers.layout.Maven2LayoutProvider
import org.carlspring.strongbox.storage.repository.RepositoryPolicyEnum
import org.carlspring.strongbox.storage.repository.RepositoryTypeEnum
import org.carlspring.strongbox.storage.repository.RepositoryStatusEnum

def artifactClient = ArtifactClient.testInstance;
def restClient = RestClient.getTestInstanceLoggedInAsAdmin()

System.out.println()
System.out.println()
System.out.println("Host name: " + restClient.getHost())
System.out.println("Username:  " + restClient.getUsername())
System.out.println("Password:  " + restClient.getPassword())
System.out.println()
System.out.println()

try
{
    def configuration = restClient.getConfiguration()

    System.out.println()
    System.out.println("configuration = " + configuration)
    System.out.println()

    def storage = configuration.getStorage("storage0")

    System.out.println()
    System.out.println("storage = " + storage.toString())
    System.out.println()

    def repositoryId = "releases-with-trash"

    if (storage.getRepository(repositoryId) == null)
    {

        System.out.println()
        System.out.println()
        System.out.println("Creating a new repository with ID '" + repositoryId + "'...")
        System.out.println()
        System.out.println()

        // Create the test repository:
        def repository = new RepositoryForm()
        repository.setId(repositoryId)
        repository.setLayout(Maven2LayoutProvider.ALIAS)
        repository.setPolicy(RepositoryPolicyEnum.RELEASE.policy)
        repository.setTrashEnabled(true)
        repository.setImplementation(StorageProviderEnum.FILESYSTEM.description)
        repository.setType(RepositoryTypeEnum.HOSTED.type)
        repository.setStatus(RepositoryStatusEnum.IN_SERVICE.status)

        restClient.addRepository(repository, storage.getId())

        System.out.println()
        System.out.println()
        System.out.println("Repository '" + repositoryId + "' successfully created!")
        System.out.println()
        System.out.println()
    } else {

        def path = "/storages/storage0/releases-with-trash/org/carlspring/maven/test-project/1.0.3"

        if (artifactClient.pathExists(path))
        {
            artifactClient.delete("storage0", "releases-with-trash", "org/carlspring/maven/test-project/1.0.3");
        }
        artifactClient.deleteTrash("storage0", "releases-with-trash");
    }
    
}
catch (Exception e)
{
    e.printStackTrace()
    return false
}
