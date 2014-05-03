# -*- coding: utf-8 -*-
from multiprocessing import Process,Lock
from numpy import float32,dot, divide, int32, rint
import numpy
import shmarray


def worker(p,loadPerWorker, projections, combined_matrix, z_voxel_coords,
            transform_matrix, z_voxels, detector_rows, detector_columns,
            recon_volume, volume_weight):
    for ii in xrange(loadPerWorker):
        flat_proj_data = projections[p+ii].ravel()

        for z in xrange(z_voxels):

    # Put current z voxel into combined_matrix
            combined_matrix[2, :] = z_voxel_coords[z]

    # Find the mapping between volume voxels and detector pixels
    # for the current angle
            vol_det_map = dot(transform_matrix[p+ii], combined_matrix)
            map_cols = rint(divide(vol_det_map[0, :], vol_det_map[2, :
                            ])).astype(int32)
            map_rows = rint(divide(vol_det_map[1, :], vol_det_map[2, :
                            ])).astype(int32)

    # Find the detector pixels that contribute to the current slice
    # xrays that hit outside the detector area are masked out
            mask = (map_cols >= 0) & (map_rows >= 0) & (map_cols
                    < detector_columns) & (map_rows < detector_rows)

    # The projection pixels that contribute to the current slice
            proj_indexs = map_cols * mask + map_rows * mask \
                    * detector_columns

    # Add the weighted projection pixel values to their
    # corresponding voxels in the z slice
            #r = recon_volume[(p/32),:,:,:]
            recon_volume[z].flat += flat_proj_data[proj_indexs] \
                    * volume_weight[p+ii] * mask





def fdkcore(nr_projections, projections, combined_matrix, z_voxel_coords,
            transform_matrix, z_voxels, detector_rows, detector_columns,
            recon_volume, volume_weight, count_out):

    NoWorkers = 16
    loadPerWorker = nr_projections/NoWorkers
    recon_volume = [shmarray.zeros((z_voxels, z_voxels, z_voxels), dtype=float32)
            for i in xrange(NoWorkers)]
    processes = [Process(target=worker, args=((i*loadPerWorker), loadPerWorker, projections, combined_matrix, z_voxel_coords,
            transform_matrix, z_voxels, detector_rows, detector_columns,
            recon_volume[i], volume_weight)) \
            for i in xrange(NoWorkers)]
    for p in processes:
        p.start()
    for p in processes:
        p.join()
    return numpy.add.reduce(recon_volume, axis=0)