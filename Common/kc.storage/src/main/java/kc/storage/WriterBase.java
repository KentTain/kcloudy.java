package kc.storage;

import java.util.Map;

public abstract class WriterBase extends BlobBase
{
    public WriterBase()
    {
    }

    protected abstract void CreateContainer(String container);

    public void AddContainer(String container)
    {
        this.CreateContainer(container);

    }

    protected abstract void DeleteContainer(String container);

    public void RemoveContainer(String container)
    {
        this.DeleteContainer(container);
    }

    protected abstract void WriteBlob(String container, String blobId, byte[] blobData, Map<String, String> metadata);

    public void SaveBlob(String container, String blobId, byte[] blobData, Map<String, String> metadata)
    {
        this.WriteBlob(container, blobId, blobData, metadata);

        //LogUtil.LogDebug("Saved Blob", "BlobId=" + blobId + " Size=" + blobData.Length, stopwatch.ElapsedMilliseconds);

        //if (this.BlobSaved != null)
        //    this.BlobSaved(this, new AzureBlobEventArgs(container, blobId, true, metadata != null));
    }

    protected abstract void DeleteBlob(String container, String blobId);

    public void RemoveBlob(String container, String blobId)
    {
        this.DeleteBlob(container, blobId);
    }

    public abstract void CopyBlob(String containerName, String desContainerName, String blobId);

    protected abstract void WriteBlobMetadata(String container, String blobId, boolean clearExisting, Map<String, String> blobMetadata);

    /// <summary>Merge blob metadata</summary>
    public void AppendBlobMetadata(String container, String blobId, Map<String, String> blobMetadata)
    {
        this.WriteBlobMetadata(container, blobId, false, blobMetadata);
    }

    /// <summary>Will overwrite blob metadata</summary>
    public void SaveBlobMetadata(String container, String blobId, Map<String, String> blobMetadata)
    {
        this.WriteBlobMetadata(container, blobId, true, blobMetadata);
    }


    protected abstract void WriteBlobBlock(String container, String blobId, String blobBlockId, byte[] blobData, String contentMD5);

    public void SaveBlobBlock(String container, String blobId, String blobBlockId, byte[] blobData, String contentMD5)
    {
        this.WriteBlobBlock(container, blobId, blobBlockId, blobData, contentMD5);
    }


    protected abstract void SubmitBlobBlock(String container, String blobId, String[] blobBlockIds, Map<String, String> blobMetadata);

    public void CommitBlobBlock(String container, String blobId, String[] blobBlockIds, Map<String, String> blobMetadata)
    {
        this.SubmitBlobBlock(container, blobId, blobBlockIds, blobMetadata);
    }
}
